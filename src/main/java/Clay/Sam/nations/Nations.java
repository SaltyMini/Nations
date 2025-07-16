package Clay.Sam.nations;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.Nullable;

public final class Nations extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("Nations plugin enabled - WaterWorldGenerator available");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new WaterWorldGenerator();
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
