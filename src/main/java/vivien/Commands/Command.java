package vivien.Commands;

import java.util.ArrayList;
import java.util.function.Function;

import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import reactor.core.publisher.Mono;

public abstract class Command implements Function<MessageCreateEvent, Mono<Void>> {
    public String getName() {
        return null;
    }

    public String getDescription() {
        return "*No information available.*";
    }

    public ApplicationCommandOptionData[] getOptions() {
        return null;
    }

    public Command() {
        return;
    }

    public String[] getCommandParams(MessageCreateEvent event) {
        // Check parameters
        String msg_content = event.getMessage().getContent();
        System.out.println("Checking params for " + msg_content);
        String msg_rest = msg_content.substring(getName().length() + 1);
        System.out.println("Found rest of " + msg_rest);
        String[] args = msg_rest.split(" ");
        if (args.length == 0) {
            return null;
        }
        ArrayList<String> params = new ArrayList<String>();
        for (String arg : args) {
            if (arg.strip() == "") {
                continue;
            }
            params.add(arg);
        }
        String[] output = new String[params.size()];
        output = params.toArray(output);
        return output;
    }

    public Mono<Void> apply(MessageCreateEvent event) {
        System.out.println("> Executing Command: " + getName());

        return Mono.empty();
    }

    public void apply(SlashCommandEvent event) {
        System.out.println("> Executing Slash Command: " + getName());
        return;
    }
}
