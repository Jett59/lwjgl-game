package app.cleancode.game.block;

import app.cleancode.game.Node;
import app.cleancode.game.World;

public class Block extends Node {
    private int x, y, z;

    public Block(int blockId, World world, int initialX, int initialY, int initialZ) {
        super(Blocks.getBlock(blockId));
        x = initialX;
        y = initialY;
        z = initialZ;
        world.put(this, x, y, z);
        setTranslateX(x);
        setTranslateY(y);
        setTranslateZ(z);
    }

    public void move(World world, int x, int y, int z) {
        world.put(this, x, y, z);
        world.remove(this.x, this.y, this.z);
        this.x = x;
        this.y = y;
        this.z = z;
        setTranslateX(x);
        setTranslateY(y);
        setTranslateZ(z);
    }

}
