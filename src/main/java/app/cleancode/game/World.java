package app.cleancode.game;

import app.cleancode.game.block.Block;
import app.cleancode.game.block.BlockIds;

public class World {
    private static final int xMax = 256;
    private static final int yMax = 256;
    private static final int zMax = 256;

    private static final int xAdjust = xMax / 2;
    private static final int yAdjust = yMax / 2;
    private static final int zAdjust = zMax / 2;

    private Block worldEdge;
    private Block[][][] blocks;

    public World() {
        blocks = new Block[xMax][yMax][zMax];
        worldEdge = new Block(BlockIds.barrier, this, xMax, yMax, zMax);
    }

    public void put(Block block, int x, int y, int z) {
        try {
            blocks[x + xAdjust][y + yAdjust][z + zAdjust] = block;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public void remove(int x, int y, int z) {
        put(null, x, y, z);
    }

    public Block blockAt(int x, int y, int z) {
        try {
            return blocks[x + xAdjust][y + yAdjust][z + zAdjust];
        } catch (ArrayIndexOutOfBoundsException e) {
            return worldEdge;
        }
    }

    public int surfaceLevelOf(int x, int z) {
        for (int y = yMax - 1 - yAdjust; y >= -yAdjust; y--) {
            if (blockAt(x, y, z) != null) {
                return y + 1;
            }
        }
        return Integer.MIN_VALUE;
    }
}
