package app.cleancode.game.terrain;

import java.util.Random;
import app.cleancode.game.Scene;
import app.cleancode.game.World;
import app.cleancode.game.block.Block;
import app.cleancode.game.block.BlockIds;

public class TerrainGenerator {
    private final Random rand = new Random();

    private void generateHill(int x, int z, int elevation, int minRadius, World world,
            Scene scene) {
        for (int currentElevation = elevation; currentElevation >= 0; currentElevation--) {
            int levelRadius = minRadius + 2 * (elevation - currentElevation);
            double angleIncrement = 360d / (2 * Math.PI * levelRadius);
            for (double angle = 0; angle < 360; angle += angleIncrement) {
                double radians = Math.toRadians(angle);
                int blockX = (int) Math.round(Math.cos(radians) * levelRadius);
                int blockZ = (int) Math.round(Math.sin(radians) * levelRadius);
                if (world.blockAt(blockX, currentElevation, blockZ) == null) {
                    scene.add(new Block(BlockIds.grass, world, blockX, currentElevation, blockZ));
                }
            }
        }
    }

    public void generateTerrain(Scene scene, World world) {
        for (int x = -16; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = -16; z < 16; z++) {
                    scene.add(new Block(BlockIds.grass, world, x, -y, z));
                }
            }
        }
        int numHills = rand.nextInt(5) + 1;
        for (int i = 0; i < numHills; i++) {
            generateHill(rand.nextInt(33) - 16, rand.nextInt(33) - 16, rand.nextInt(5) + 1,
                    rand.nextInt(10) + 1, world, scene);
        }
    }
}
