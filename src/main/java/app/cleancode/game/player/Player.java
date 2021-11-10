package app.cleancode.game.player;

import app.cleancode.game.World;
import app.cleancode.gl.GlCamera;

public class Player {
    private final GlCamera camera;
    private float x, y, z;

    public Player(GlCamera camera) {
        this.camera = camera;
    }

    public void move(float deltaX, float deltaY, float deltaZ, World world) {
        if (world.blockAt((int) (x + deltaX), (int) (y + deltaY), (int) (z + deltaZ)) == null) {
            x += deltaX;
            y += deltaY;
            z += deltaZ;
            camera.move(deltaX, deltaY, deltaZ);
        }
    }
}
