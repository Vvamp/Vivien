package vivien.Commands;

import java.util.function.Function;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public abstract class Command implements Function<MessageCreateEvent, Mono<Void>> {
    public String getName() {
        return null;
    }

    public String getDescription() {
        return "*No information available.*";
    }

    public Command() {
        System.out.println("> Executing Command: " + getName());
        return;
    }

    public void apply(SlashCommandEvent event) {
        return;
    }
}
