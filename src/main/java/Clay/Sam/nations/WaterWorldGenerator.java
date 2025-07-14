package Clay.Sam.nations;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WaterWorldGenerator extends ChunkGenerator {

    // Island configuration
    private static final int ISLAND_SPACING = 200; // Distance between island centers
    private static final int ISLAND_RADIUS = 50;   // Maximum island radius
    private static final int SEA_LEVEL = 63;
    private static final int MAX_ISLAND_HEIGHT = 80;

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        // Generate bedrock at bottom
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
            }
        }

        // Process each block in the chunk
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;

                // Find nearest island center
                int islandCenterX = Math.round((float) worldX / ISLAND_SPACING) * ISLAND_SPACING;
                int islandCenterZ = Math.round((float) worldZ / ISLAND_SPACING) * ISLAND_SPACING;

                // Calculate distance from island center
                double distanceFromCenter = Math.sqrt(
                        Math.pow(worldX - islandCenterX, 2) +
                                Math.pow(worldZ - islandCenterZ, 2)
                );

                // Generate island if within radius
                if (distanceFromCenter <= ISLAND_RADIUS) {
                    generateIslandTerrain(chunkData, biome, x, z, worldX, worldZ,
                            islandCenterX, islandCenterZ, distanceFromCenter, world, random);
                } else {
                    // Generate ocean
                    generateOcean(chunkData, biome, x, z, world);
                }
            }
        }

        return chunkData;
    }

    private void generateIslandTerrain(ChunkData chunkData, BiomeGrid biome,
                                       int x, int z, int worldX, int worldZ,
                                       int islandCenterX, int islandCenterZ,
                                       double distanceFromCenter, World world, Random random) {

        // Safety check to prevent division by zero
        if (ISLAND_RADIUS <= 0) {
            generateOcean(chunkData, biome, x, z, world);
            return;
        }

        // Create height based on distance from center (dome shape)
        // Clamp distance to prevent negative height factors
        int terrainHeight = getTerrainHeight(worldX, worldZ, distanceFromCenter);

        // Set biome based on terrain height (once per x,z coordinate)
        Biome blockBiome;
        if (terrainHeight > SEA_LEVEL + 2) {
            blockBiome = Biome.PLAINS;
        } else if (terrainHeight >= SEA_LEVEL - 2) { // Fixed: use >= and reasonable threshold
            blockBiome = Biome.BEACH;
        } else {
            blockBiome = Biome.SWAMP; // Better than OCEAN for low island areas
        }

        // Set biome for all y levels at this x,z
        for (int y = 0; y < world.getMaxHeight(); y++) {
            biome.setBiome(x, y, z, blockBiome);
        }

        // Generate terrain layers
        for (int y = 1; y < world.getMaxHeight(); y++) {
            if (y <= terrainHeight) {
                // Determine block type based on height
                Material blockType;
                if (y == terrainHeight && terrainHeight > SEA_LEVEL + 2) {
                    blockType = Material.GRASS_BLOCK;
                } else if (y >= terrainHeight - 3 && terrainHeight > SEA_LEVEL + 2) {
                    blockType = Material.DIRT;
                } else if (y >= terrainHeight - 1 && terrainHeight >= SEA_LEVEL - 2 && terrainHeight <= SEA_LEVEL + 2) {
                    blockType = Material.SAND; // Fixed: better condition for sand
                } else {
                    blockType = Material.STONE;
                }

                chunkData.setBlock(x, y, z, blockType);
            } else if (y <= SEA_LEVEL) {
                // Fill with water up to sea level
                chunkData.setBlock(x, y, z, Material.WATER);
            }
        }

        // Fill any gaps between low terrain and sea level with water
        if (terrainHeight < SEA_LEVEL) {
            for (int y = terrainHeight + 1; y <= SEA_LEVEL; y++) {
                chunkData.setBlock(x, y, z, Material.WATER);
            }
        }
    }

    private static int getTerrainHeight(int worldX, int worldZ, double distanceFromCenter) {
        double clampedDistance = Math.min(distanceFromCenter, ISLAND_RADIUS);
        double heightFactor = 1.0 - (clampedDistance / ISLAND_RADIUS);
        heightFactor = Math.pow(heightFactor, 2); // Make it more dome-like

        // Add some noise for natural variation using world coordinates for consistency
        Random coordRandom = new Random(worldX * 31L + worldZ * 17L);
        double noise = (coordRandom.nextDouble() - 0.5) * 0.3;
        heightFactor += noise;
        heightFactor = Math.max(0, Math.min(1, heightFactor));

        int terrainHeight = (int) (SEA_LEVEL + (MAX_ISLAND_HEIGHT - SEA_LEVEL) * heightFactor);
        return terrainHeight;
    }

    private void generateOcean(ChunkData chunkData, BiomeGrid biome, int x, int z, World world) {
        // Set ocean biome for all y levels
        for (int y = 0; y < world.getMaxHeight(); y++) {
            biome.setBiome(x, y, z, Biome.OCEAN);
        }

        // Fill with water from y = 1 to sea level
        for (int y = 1; y <= SEA_LEVEL; y++) {
            chunkData.setBlock(x, y, z, Material.WATER);
        }

        // Fill with stone y = SEA_LEVEL + 1 to sea level - 30
        for(int y = 1; y < SEA_LEVEL - 30; y++) {
            chunkData.setBlock(x, y, z, Material.STONE);
        }
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public @Nullable Location getFixedSpawnLocation(World world, Random random) {
        // Find the nearest island center to (0,0)
        int islandCenterX = Math.round(0f / ISLAND_SPACING) * ISLAND_SPACING;
        int islandCenterZ = Math.round(0f / ISLAND_SPACING) * ISLAND_SPACING;

        // Calculate expected height at island center
        int expectedHeight = MAX_ISLAND_HEIGHT;

        return new Location(world, islandCenterX, expectedHeight + 1, islandCenterZ);
    }
}