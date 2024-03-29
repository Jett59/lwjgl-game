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
        for (int currentElevation = elevation; currentElevation > 0; currentElevation--) {
            int levelRadius = minRadius - (currentElevation - elevation);
            double angleIncrement = 360d / (2 * Math.PI * levelRadius) / 2;
            for (int currentRadius = levelRadius; currentRadius >= 0; currentRadius--) {
                for (double angle = 0; angle < 360; angle += angleIncrement) {
                    double radians = Math.toRadians(angle);
                    int blockX = (int) Math.round(Math.cos(radians) * currentRadius) + x;
                    int blockZ = (int) Math.round(Math.sin(radians) * currentRadius) + z;
                    if (world.blockAt(blockX, currentElevation, blockZ) == null) {
                        scene.add(
                                new Block(BlockIds.grass, world, blockX, currentElevation, blockZ));
                    }
                }
            }
        }
    }

    public void generateTerrain(Scene scene, World world) {
        int numHills = rand.nextInt(128) + 2;
        for (int i = 0; i < numHills; i++) {
            generateHill(rand.nextInt(256) - 128, rand.nextInt(256) - 128, rand.nextInt(10) + 1,
                    rand.nextInt(32) + 1, world, scene);
        }
    }
}
