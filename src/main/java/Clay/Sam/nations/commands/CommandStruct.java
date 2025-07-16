package Clay.Sam.nations.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandStruct {

    public abstract String getName(); //name of command
    public abstract String getDescription(); //description of command
    public abstract String getSyntax(); //correct command syntax
    public abstract String[] getAliases(); //aliases for command

    public abstract void commandRun(CommandSender commandSender, String[] args); //Runs the command with the given arguments

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>(); // Default empty implementation
    }

}
