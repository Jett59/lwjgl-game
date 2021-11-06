package app.cleancode.game;

public class Block extends Node {
    private BlockPosition position;

    public Block(int blockId, World world, BlockPosition initialPosition) {
        super(Blocks.getBlock(blockId));
        position = initialPosition;
        world.put(this, initialPosition);
        setTranslateX(position.x());
        setTranslateY(position.y());
        setTranslateZ(position.z());
    }

    public void move(World world, BlockPosition position) {
        world.put(this, position);
        world.remove(this.position);
        this.position = position;
        setTranslateX(position.x());
        setTranslateY(position.y());
        setTranslateZ(position.z());
    }

}
