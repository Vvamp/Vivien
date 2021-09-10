package vivien.Commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.rest.util.ApplicationCommandOptionType;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PurgeCommand extends Command {
    // TODO: Optionally give channel
    // TODO: Faster deletion(maybe store in a list and bulkDelete?)

    public String getName() {
        return "purge";
    }

    public String getDescription() {
        return "Purges a channel of messages";
    }

    public PurgeCommand() {
        super();
    }

    private ApplicationCommandOptionData[] options = { ApplicationCommandOptionData.builder().name("amount")
            .required(false).description("the amount of messages to delete")
            .type(ApplicationCommandOptionType.INTEGER.getValue()).build() };

    public ApplicationCommandOptionData[] getOptions() {
        return options;
    }

    @Override
    public Mono<Void> apply(MessageCreateEvent event) {
        super.apply(event);

        // Check permissions
        PermissionSet permissions = event.getMessage().getAuthor().orElseThrow()
                .asMember(event.getGuild().block().getId()).block().getBasePermissions().block();
        if (permissions.contains(Permission.MANAGE_CHANNELS) == false
                && permissions.contains(Permission.ADMINISTRATOR) == false) {
            // If initiator has no manage channels permission
            event.getMessage().getChannel().block()
                    .createMessage("Sorry, you don't have permission to use this command.").block();
            return Mono.empty();
        }

        String[] args = getCommandParams(event);
        int amount = -1;
        if (args.length > 0) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("> Failed to convert parameter: " + e.getMessage());
                amount = -1;
            }
        }

        // Purge
        if (purge(event.getMessage().getChannel().block(), event.getMessage().getId(), amount) == false) {
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
        int count = -1;
        // Check if the user set an amount
        try {
            count = Integer.parseInt(event.getOption("amount").orElseThrow().getValue().orElseThrow().getRaw());
            System.out.println("> Set purge count to " + count);
        } catch (Exception e) {
            System.out.println("> Failed to get option: " + e.getMessage());
            count = -1;
        }
        // Get user permissions
        PermissionSet permissions = event.getInteraction().getUser()
                .asMember(event.getInteraction().getGuild().block().getId()).block().getBasePermissions().block();
        // Check if user has the manage channel perm
        if (permissions.contains(Permission.MANAGE_CHANNELS) == false
                && permissions.contains(Permission.ADMINISTRATOR) == false) {
            // If initiator has no manage channels permission
            event.getInteraction().getChannel().block()
                    .createMessage("Sorry, you don't have permission to use this command.").block();
            return;
        }
        // Purge
        Snowflake lastMessageID = event.getInteraction().getChannel().block().getLastMessage().block().getId();
        if (purge(event.getInteraction().getChannel().block(), lastMessageID, count) == false) {
            event.getInteraction().getChannel().block().createMessage(
                    "Sorry, I was unable to assist you with this request. Perhaps you could check my permissions?")
                    .block();
        }

    }

    private boolean purge(MessageChannel channel, Snowflake lastMessage) {
        // purge all messages
        return purge(channel, lastMessage, -1);
    }

    private boolean purge(MessageChannel channel, Snowflake lastMessage, int count) {
        Flux<Message> messagesToPurge = channel.getMessagesBefore(lastMessage);
        try {
            int currentCount = 0;
            for (Message msg : messagesToPurge.collectList().block()) {
                System.out.println("> PURGE: Deleting message: " + msg.getContent());
                msg.delete("Purge").block();
                currentCount++;
                if (currentCount >= count && count != -1) {
                    break;
                }
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
