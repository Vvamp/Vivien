package vivien.Modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest.Builder;
import discord4j.rest.RestClient;
import reactor.core.publisher.Mono;
import vivien.Commands.AboutCommand;
import vivien.Commands.Command;
import vivien.Commands.PurgeCommand;

public class CommandManager {

    private static final Map<String, Command> commands = new HashMap<>();
    private static final Command[] commandList = { new AboutCommand(), new PurgeCommand() }; // List of command classes.
                                                                                             // Add all command

    private static long applicationid;
    private static RestClient rc;

    public CommandManager(long appid, RestClient restClient) {
        applicationid = appid;
        rc = restClient;
    }

    public void register() {
        // For each command class in the commandList, make a normal command and attempt
        // to make a slash command

        // Create new commands
        for (Command command : commandList) {
            System.out.print("Attempting to create: " + command.getName() + "...");

            // Create command
            Builder currentCommandBuilder = ApplicationCommandRequest.builder().name(command.getName())
                    .description(command.getDescription());

            if (command.getOptions() != null) {
                for (ApplicationCommandOptionData option : command.getOptions()) {
                    currentCommandBuilder.addOption(option);
                }
            }

            ApplicationCommandRequest currentCommand = currentCommandBuilder.build();
            // Register command
            ArrayList<ApplicationCommandRequest> currentCommandList = new ArrayList<ApplicationCommandRequest>();
            currentCommandList.add(currentCommand);

            rc.getApplicationService().createGlobalApplicationCommand(applicationid, currentCommand)
                    .doOnSuccess(e -> System.out.println(" ...Success creating command!"))
                    .doOnError(e -> System.out.println(" ...Unable to create global command:" + e.getMessage()))
                    .onErrorResume(e -> Mono.empty()).block();

            // Make it accessible
            commands.put(command.getName(), command);

        }
    }

    public Command[] getCommandList() {
        return commandList;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void addCommandToList(Command command) {
        commands.put(command.getName(), command);
    }
}
