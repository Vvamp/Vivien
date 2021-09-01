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

    private final String about_message = "My name is Vivien. I was made by Vvamp(in Discord4J) to assist in the moderation of this server!";

    @Override
    public Mono<Void> apply(MessageCreateEvent event) {
        super.apply(event);
        event.getMessage().getChannel().block().createMessage(about_message).block();
        return Mono.empty();
    }

    @Override
    public void apply(SlashCommandEvent event) {
        super.apply(event);
        event.reply(about_message).block();
    }

}
