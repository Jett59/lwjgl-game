package app.cleancode.game.player;

import app.cleancode.game.World;
import app.cleancode.gl.GlCamera;

public class Player {
    private static final float height = 1.5f;

    private final GlCamera camera;
    private float x, y, z;
    private float yRotate;

    public float xVelocity = 0, yVelocity = 0, zVelocity = 0;

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
        if (!isBlockPresent(newX, y, z, world)) {
            x = newX;
            camera.move(newX, y + height, z);
        }
        if (!isBlockPresent(x, newY, z, world)) {
            y = newY;
            camera.move(x, newY + height, z);
        }
        if (!isBlockPresent(x, y, newZ, world)) {
            z = newZ;
            camera.move(x, y + height, newZ);
        }
    }

    public void rotate(float deltaX, float deltaY) {
        yRotate += deltaY;
        camera.rotate(deltaX, deltaY);
    }

    public boolean isTouchingGround(World world, float distance) {
        return isBlockPresent(x, y - distance, z, world);
    }
}
