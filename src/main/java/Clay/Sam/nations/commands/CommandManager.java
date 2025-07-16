package Clay.Sam.nations.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<CommandStruct> commandStruct = new ArrayList<>(); //list of all commands

    public CommandManager() {
        //commandStruct.add(new GiveCommand()); //adds example command to command structure
        //commandStruct.add(new help()); //adds help command to command structure
        //TODO: add new commands
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //Here I have done two implementations which can be used separately or together


        //This implementation is for prefixing all commands with a keyword, in this example customcommand.
        //This would result in the commands looking like
        // /customcommands examplecommand
        // /customcommands help
        if(command.getLabel().equalsIgnoreCase("nations")) {

            // Check if args is empty
            if (args.length == 0) {
                commandSender.sendMessage("Use '/customcommands help' to see available commands");
                return true;
            }

            for (CommandStruct cmd : getCommandStructNations()) {
                if (args[0].equalsIgnoreCase(cmd.getName()) //This checks if the args[0] exists as a commandName
                        || Arrays.asList(cmd.getAliases()).contains(args[0])) { //This checks if the args[0] exists as an alias
                    cmd.commandRun(commandSender, args);
                }
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (CommandStruct cmd : commandStruct) {
                completions.add(cmd.getName());
            }
            return completions;
        }

        // Find the appropriate subcommand and delegate tab completion
        String subCommand = args[0].toLowerCase();
        for (CommandStruct cmd : commandStruct) {
            if (cmd.getName().equalsIgnoreCase(subCommand)) {
                return cmd.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return new ArrayList<>();
    }

    public ArrayList<CommandStruct> getCommandStructNations() {
        return commandStruct;
    } // returns the command list
}