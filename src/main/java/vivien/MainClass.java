package vivien;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Publisher;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.interaction.SlashCommandEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import io.github.cdimascio.dotenv.Dotenv;
import reactor.core.publisher.Mono;
import vivien.Commands.AboutCommand;
import vivien.Commands.Command;
import vivien.Modules.BillBoard;

class MainClass {
    private static final Map<String, Command> commands = new HashMap<>();
    private static final char command_prefix = '!';
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Command[] commandList = { new AboutCommand() }; // List of command classes. Add all command
                                                                         // classes here, the rest gets updated
                                                                         // automatically

    public static void main(final String[] args) {
        Dotenv dotenv = Dotenv.load();
        GatewayDiscordClient client = DiscordClientBuilder.create(dotenv.get("CLIENT_TOKEN")).build().login().block();

        // Handle Login
        client.getEventDispatcher().on(ReadyEvent.class).subscribe(event -> {
            final User self = event.getSelf();
            System.out.println(String.format("> Logged in as %s#%s", self.getUsername(), self.getDiscriminator()));
        });

        // Timed Tasks
        BillBoard bb = new BillBoard(client);
        scheduler.scheduleAtFixedRate(bb, 0, 1, TimeUnit.MINUTES);

        // For each command class in the commandList, make a normal command and attempt
        // to make a slash command
        RestClient rc = client.getRestClient();
        long applicationid = rc.getApplicationId().block();
        for (Command c : commandList) {
            ApplicationCommandRequest currentCommand = ApplicationCommandRequest.builder().name(c.getName())
                    .description(c.getDescription()).build();
            System.out.println(c.getName());
            rc.getApplicationService()
                    .createGuildApplicationCommand(applicationid, Snowflake.asLong("843501487036563487"),
                            currentCommand)
                    .doOnError(e -> System.out.println("Unable to create guild command:" + e.getMessage()))
                    .onErrorResume(e -> Mono.empty()).block();
            commands.put(c.getName(), c);

        }

        // Handle commands
        client.on(new ReactiveEventAdapter() {
            public Publisher<?> onMessageCreate(MessageCreateEvent event) {
                if (event.getMessage().getContent().startsWith(String.valueOf(command_prefix))) {
                    String word = event.getMessage().getContent().split(" ")[0]; // Remove any trailing params
                    String command = word.subSequence(1, word.length()).toString(); // Remove the command prefix
                    commands.get(command).apply(event); // Apply command
                }
                return Mono.empty();
            };

            @Override
            public Publisher<?> onSlashCommand(SlashCommandEvent event) {
                commands.get(event.getCommandName()).apply(event); // Apply command by name
                return Mono.empty();
            }
        }).blockLast();

        client.onDisconnect().block();
    }
}
