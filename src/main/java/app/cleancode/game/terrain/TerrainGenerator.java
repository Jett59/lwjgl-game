package app.cleancode.game.terrain;

import app.cleancode.game.Scene;
import app.cleancode.game.World;
import app.cleancode.game.block.Block;
import app.cleancode.game.block.BlockIds;

public class TerrainGenerator {
    public static void generateTerrain(Scene scene, World world) {
        for (int x = -16; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = -16; z < 16; z++) {
                    scene.add(new Block(BlockIds.grass, world, x, -2 - y, z));
                }
            }
        }
    }
}
