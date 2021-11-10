package app.cleancode.game.player;

import app.cleancode.game.World;
import app.cleancode.gl.GlCamera;

public class Player {
    private final GlCamera camera;
    private float x, y, z;
    private float yRotate;

    public Player(GlCamera camera) {
        this.camera = camera;
    }

    private boolean isBlockPresent(float x, float y, float z, World world) {
        return world.blockAt((int) Math.round(x), (int) Math.round(y), (int) Math.round(z)) != null;
    }

    public void move(float deltaX, float deltaY, float deltaZ, World world) {
        float newX = x, newY = y, newZ = z;
        if (deltaX != 0) {
            newX += (float) Math.sin(Math.toRadians(yRotate - 90)) * -1.0f * deltaX;
            newZ += (float) Math.cos(Math.toRadians(yRotate - 90)) * deltaX;
        }
        if (deltaZ != 0) {
            newX += (float) Math.sin(Math.toRadians(yRotate)) * -1.0f * deltaZ;
            newZ += (float) Math.cos(Math.toRadians(yRotate)) * deltaZ;
        }
        newY += deltaY;
        if (!isBlockPresent(newX, newY, newZ, world)) {
            x = newX;
            y = newY;
            z = newZ;
            camera.move(x, y + 1.5f, z);
        }
    }

    public void rotate(float deltaX, float deltaY) {
        yRotate += deltaY;
        camera.rotate(deltaX, deltaY);
    }

    public boolean isTouchingGround(World world) {
        return isBlockPresent(x, y - 1, z, world);
    }
}
