package vivien.Commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PurgeCommand extends Command {
    // TODO: Enter amount and optionally channel

    public String getName() {
        return "purge";
    }

    public String getDescription() {
        return "Purges a channel of messages";
    }

    public PurgeCommand() {
        super();

    }

    @Override
    public Mono<Void> apply(MessageCreateEvent event) {
        super.apply(event);
        PermissionSet permissions = event.getMessage().getAuthor().orElseThrow()
                .asMember(event.getGuild().block().getId()).block().getBasePermissions().block();
        if (permissions.contains(Permission.MANAGE_CHANNELS) == false
                && permissions.contains(Permission.ADMINISTRATOR) == false) {
            // If initiator has no manage channels permission
            event.getMessage().getChannel().block()
                    .createMessage("Sorry, you don't have permission to use this command.").block();
            return Mono.empty();
        }

        if (purge(event.getMessage().getChannel().block(), event.getMessage().getId()) == false) {
            event.getMessage().getChannel().block().createMessage(
                    "Sorry, I was unable to assist you with this request. Perhaps you could check my permissions?")
                    .block();
        }

        return Mono.empty();
    }

    @Override
    public void apply(SlashCommandEvent event) {
        super.apply(event);
        event.replyEphemeral("Purging...").block();
        PermissionSet permissions = event.getInteraction().getUser()
                .asMember(event.getInteraction().getGuild().block().getId()).block().getBasePermissions().block();
        if (permissions.contains(Permission.MANAGE_CHANNELS) == false
                && permissions.contains(Permission.ADMINISTRATOR) == false) {
            // If initiator has no manage channels permission
            event.getInteraction().getChannel().block()
                    .createMessage("Sorry, you don't have permission to use this command.").block();
            return;
        }

        Snowflake lastMessageID = event.getInteraction().getChannel().block().getLastMessage().block().getId();
        if (purge(event.getInteraction().getChannel().block(), lastMessageID) == false) {
            event.getInteraction().getChannel().block().createMessage(
                    "Sorry, I was unable to assist you with this request. Perhaps you could check my permissions?")
                    .block();
            // event.acknowledgeEphemeral().block();
        }
        // event.acknowledgeEphemeral().block();

    }

    private boolean purge(MessageChannel channel, Snowflake lastMessage) {
        Flux<Message> messagesToPurge = channel.getMessagesBefore(lastMessage);
        try {
            for (Message msg : messagesToPurge.collectList().block()) {
                System.out.println("> PURGE: Deleting message: " + msg.getContent());
                msg.delete("Purge").block();
            }
            channel.getMessageById(lastMessage).block().delete("Purge").block();
        } catch (Exception e) {
            if (e.getMessage().contains("Unknown Message")) {
                return true;
            }
            System.out.println("> Failed to purge: " + e.getMessage());
            return false;
        }
        return true;
    }
}
