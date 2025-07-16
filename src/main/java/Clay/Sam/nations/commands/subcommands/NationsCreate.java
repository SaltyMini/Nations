package Clay.Sam.nations.commands.subcommands;

import Clay.Sam.nations.DataStorage.YMLManager;
import Clay.Sam.nations.Nations;
import Clay.Sam.nations.commands.CommandStruct;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NationsCreate extends CommandStruct {

    private final Plugin plugin;
    private final YMLManager ymlManager;

    public NationsCreate() {
        plugin = Nations.getPlugin();
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

        ymlManager.updateNationYMLFile(nationName, "name", nationName );
        ymlManager.updatePlayerYMLFile(player.getUniqueId(), "Nation", nationName);

    }
}
