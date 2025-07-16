package Clay.Sam.nations.DataStorage;

import Clay.Sam.nations.Nations;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YMLManager {


    private static YMLManager instance;
    private final Plugin plugin;



    public YMLManager() {
        plugin = Nations.getPlugin();
    }

    public static YMLManager getInstance() {
        if (instance == null) {
            instance = new YMLManager();
        }
        return instance;
    }


    public File createOrGetPlayerYML(UUID uuid) {

        File dataFile;
        FileConfiguration dataConfig;

        dataFile = new File(plugin.getDataFolder(), uuid.toString() + ".yml");

        if(plugin.getDataFolder().mkdirs()) {
            plugin.getDataFolder().mkdir();
        }

        String fileName = uuid.toString();

        // Create the file if it doesn't exist
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                plugin.getLogger().info("Created " + fileName + ".yml");
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + fileName + ".yml");
                e.printStackTrace();
            }

            dataConfig = YamlConfiguration.loadConfiguration(dataFile);

            //TODO: Because this is using none as no nation, make sure no nation can be made with the name none

            /**
                Default config
             */
            dataConfig.set("Nation.", "none");

        }

        return dataFile;

    }

    public void updatePlayerYMLFile(UUID uuid, String key, Object data) {

        File dataFile;
        FileConfiguration dataConfig;

        dataFile = createOrGetPlayerYML(uuid);
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        dataConfig.set(key, data);

        saveYML(dataFile, dataConfig);

    }

    private void saveYML(File dataFile, FileConfiguration dataConfig) {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + dataFile.getName());
            e.printStackTrace();
        }
    }

}
