package vivien.Commands;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class AboutCommand extends Command {

    public String getName() {
        return "about";
    }

    public String getDescription() {
        return "Shows information about Vivien";
    }

    public AboutCommand() {
        super();

    }

    @Override
    public Mono<Void> apply(MessageCreateEvent event) {
        final String about_message = "My name is Vivien. I was made by Vvamp(in Discord4J) to assist in the moderation of this server!";
        event.getMessage().getChannel().block().createMessage(about_message).block();
        return Mono.empty();
    }

    public void apply(SlashCommandEvent event) {
        final String about_message = "My name is Vivien. I was made by Vvamp(in Discord4J) to assist in the moderation of this server!";

        event.reply(about_message).block();
    }

}
