package Clay.Sam.nations.commands.subcommands;

import Clay.Sam.nations.DataStorage.YMLManager;
import Clay.Sam.nations.commands.CommandStruct;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NationsJoin extends CommandStruct {

    private final YMLManager ymlManager;

    public NationsJoin() {
        ymlManager = YMLManager.getInstance();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public void commandRun(CommandSender commandSender, String[] args) {

        Player player = (Player) commandSender;

        //TODO: check if nation exists
        String nationName = args[0];

        ymlManager.updatePlayerYMLFile(player.getUniqueId(), "Nation", nationName );


    }
}
