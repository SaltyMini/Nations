package Clay.Sam.nations.DataStorage;

import Clay.Sam.nations.Nations;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


    private File createOrGetPlayerYML(UUID uuid) {

        File dataFile;
        FileConfiguration dataConfig;

        dataFile = new File(plugin.getDataFolder(), uuid.toString() + ".yml");

        if(!plugin.getDataFolder().mkdirs()) {
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
            }

            dataConfig = YamlConfiguration.loadConfiguration(dataFile);

            /**
                Default config
             */
            dataConfig.set("Nation.", "none");

        }
        return dataFile;
    }

    private File createOrGetNationYML(String nationName) {

        File nationsFolder = new File(plugin.getDataFolder(), "nations");
        File dataFile = new File(nationsFolder, nationName + ".yml");

        // Create the nations folder if it doesn't exist
        if (!nationsFolder.exists()) {
            if (nationsFolder.mkdirs()) {
                plugin.getLogger().info("Created nations folder");
            } else {
                plugin.getLogger().severe("Could not create nations folder");
                return null;
            }
        }

        // Create the file if it doesn't exist
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                plugin.getLogger().info("Created " + nationName + ".yml");
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + nationName + ".yml");
                e.printStackTrace();
                return null;
            }

            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

            /**
             * Default nation config
             */
            dataConfig.set("name", nationName);
            dataConfig.set("leader", "");
            dataConfig.set("members", new ArrayList<String>());
            dataConfig.set("createdDate", System.currentTimeMillis());
            dataConfig.set("balance", 0.0);

            // Save the default configuration
            try {
                dataConfig.save(dataFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save default config for " + nationName + ".yml");
                e.printStackTrace();
            }
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

    public void updateNationYMLFile(String nationName, String key, Object data) {

        File dataFile;
        FileConfiguration dataConfig;

        dataFile = createOrGetNationYML(nationName);
        if (dataFile == null) return; // If the file creation failed, exit

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
