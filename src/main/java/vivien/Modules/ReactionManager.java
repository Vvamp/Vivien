package vivien.Modules;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;

public class ReactionManager {
    // To use unicode emoji's, use the hashcode
    private static ReactionMessage[] messages = {
            new ReactionMessage("844989841900437505", "844942837434155018", "Arthurshield", "844941638291030066"), // Rule
            new ReactionMessage("883721028063330354", "844942837434155018", "NSFWOutcast", "883717256339673108"), // NSFW
                                                                                                                  // Role
            new ReactionMessage("883726913208741888", "844942837434155018", "9989", "883726085450235984") // Notifications

    };
    private GatewayDiscordClient client;

    public ReactionManager(GatewayDiscordClient client) {
        this.client = client;
        initialize();
    }

    private void initialize() {
        // Initialize reaction upon load
        for (ReactionMessage reactionMessage : messages) {
            String channelid = reactionMessage.getChannelId();
            String messageid = reactionMessage.getId();
            Message msg = client.getMessageById(Snowflake.of(channelid), Snowflake.of(messageid)).block();
            ReactionEmoji emoji = null;
            for (Guild g : client.getGuilds().collectList().block()) {
                for (GuildEmoji m : g.getEmojis().collectList().block()) {
                    if (m.getName().equals(reactionMessage.getEmoji())) {
                        emoji = ReactionEmoji.of(m.getData());
                        break;
                    }
                }
            }

            if (emoji == null) {
                System.out.println("> Couldn't find emoji for adding reaction, skipping...");
                continue;
            }

            // React
            msg.addReaction(emoji).block();
        }
    }

    public void processReactionAdd(ReactionAddEvent event) {
        for (ReactionMessage reactionMessage : messages) {
            // Check if message id exists in reaction messages
            if (reactionMessage.getId().equals(event.getMessageId().asString())) {
                // If it does, retrieve the emoji id required(name if custom, unicode if
                // default)
                String emojiID = "";
                try {
                    emojiID = String.valueOf(event.getEmoji().asUnicodeEmoji().orElseThrow().hashCode());
                } catch (Exception e) {
                    emojiID = event.getEmoji().asCustomEmoji().orElseThrow().getName();
                }
                System.out.println("> Reaction Emoji ID: " + emojiID);

                if (emojiID.equals(reactionMessage.getEmoji())) {
                    // If the emoji matches, attempt to get the role and grant it
                    event.getMember().orElseThrow().addRole(Snowflake.of(reactionMessage.getRole())).block();
                    System.out.println("> Granted user " + event.getUser().block().getUsername() + " the role "
                            + reactionMessage.getRole());
                }
            }
        }
    }

    public void processReactionRemove(ReactionRemoveEvent event) {
        for (ReactionMessage reactionMessage : messages) {
            // Check if message id exists in reaction messages
            if (reactionMessage.getId().equals(event.getMessageId().asString())) {
                // If it does, retrieve the emoji id required(name if custom, unicode if
                // default)
                String emojiID = "";
                try {
                    emojiID = String.valueOf(event.getEmoji().asUnicodeEmoji().orElseThrow().hashCode());
                } catch (Exception e) {
                    emojiID = event.getEmoji().asCustomEmoji().orElseThrow().getName();
                }
                System.out.println("> Reaction Emoji ID: " + emojiID);
                if (emojiID.equals(reactionMessage.getEmoji())) {
                    // If the emoji matches, attempt to remove the role
                    event.getUser().block().asMember(event.getGuildId().orElseThrow()).block()
                            .removeRole(Snowflake.of(reactionMessage.getRole())).block();
                    System.out.println("> Revoked user " + event.getUser().block().getUsername() + " the role "
                            + reactionMessage.getRole());
                }
            }
        }
    }
}
