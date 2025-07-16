package Clay.Sam.nations;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WaterWorldGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        // Set biome to ocean for chunk
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biome.setBiome(x, z, Biome.OCEAN);
            }
        }

        // Generate bedrock layer (y=0)
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
            }
        }

        // Generate deepslate layer (y=1 to y=23)
        for (int y = 1; y <= 23; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, y, z, Material.DEEPSLATE);
                }
            }
        }

        // Generate stone layer (y=24 to y=38)
        for (int y = 24; y <= 38; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
            }
        }

        // Generate dirt layer (y=39 to y=43)
        for (int y = 39; y <= 43; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, y, z, Material.DIRT);
                }
            }
        }

        // Generate water layer (y=44 to y=64)
        for (int y = 44; y <= 64; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, y, z, Material.WATER);
                }
            }
        }

        return chunkData;
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public @Nullable Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 65, 0);
    }
}