package app.cleancode.game;

import java.util.HashMap;
import java.util.Map;
import app.cleancode.game.block.Block;
import app.cleancode.game.block.BlockPosition;

public class World {
    private Map<BlockPosition, Block> blocks;

    public World() {
        blocks = new HashMap<>();
    }

    public void put(Block block, BlockPosition position) {
        blocks.put(position, block);
    }

    public void remove(BlockPosition position) {
        blocks.remove(position);
    }

    public Block blockAt(BlockPosition position) {
        return blocks.get(position);
    }
}
