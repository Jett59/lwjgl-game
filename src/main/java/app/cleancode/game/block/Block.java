package app.cleancode.game.block;

import app.cleancode.game.Node;
import app.cleancode.game.World;

public class Block extends Node {
    private int x, y, z;
    private World world;

    public Block(int blockId, World world, int initialX, int initialY, int initialZ) {
        super(Blocks.getBlock(blockId));
        this.world = world;
        x = initialX;
        y = initialY;
        z = initialZ;
        world.put(this, x, y, z);
        setTranslateX(x);
        setTranslateY(y);
        setTranslateZ(z);
    }

    public void move(World world, int x, int y, int z) {
        this.world = world;
        world.put(this, x, y, z);
        world.remove(this.x, this.y, this.z);
        this.x = x;
        this.y = y;
        this.z = z;
        setTranslateX(x);
        setTranslateY(y);
        setTranslateZ(z);
    }

    @Override
    public boolean shouldRender() {
        return (world.blockAt(x - 1, y, z) == null || world.blockAt(x, y - 1, z) == null
                || world.blockAt(x, y, z - 1) == null || world.blockAt(x + 1, y, z) == null
                || world.blockAt(x, y + 1, z) == null || world.blockAt(x, y, z + 1) == null)
                && super.shouldRender();
    }

}
