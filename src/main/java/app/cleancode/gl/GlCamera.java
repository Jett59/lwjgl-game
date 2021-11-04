package app.cleancode.gl;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GlCamera {
    private final Matrix4f matrix;
    private Vector3f translation;
    private final Vector3f rotation;
    private boolean changed;

    public GlCamera() {
        matrix = new Matrix4f();
        translation = new Vector3f();
        rotation = new Vector3f();
        changed = true;
    }

    public void move(float deltaX, float deltaY, float deltaZ) {
        if (deltaX != 0) {
            translation.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * deltaX;
            translation.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * deltaX;
        }
        if (deltaZ != 0) {
            translation.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * deltaZ;
            translation.z += (float) Math.cos(Math.toRadians(rotation.y)) * deltaZ;
        }
        translation.y += deltaY;
        changed = true;
    }

    public void rotate(float deltaX, float deltaY, float deltaZ) {
        rotation.x += deltaX;
        rotation.y += deltaY;
        rotation.z += deltaZ;
        rotation.x %= 360f;
        rotation.y %= 360f;
        rotation.z %= 360f;
        changed = true;
    }

    public Matrix4f getMatrix() {
        if (changed) {
            changed = false;
            matrix.identity();
            return matrix.rotateX((float) Math.toRadians(rotation.x))
                    .rotateY((float) Math.toRadians(rotation.y))
                    .rotateZ((float) Math.toRadians(rotation.z))
                    .translate(-translation.x, -translation.y, -translation.z);
        } else {
            return matrix;
        }
    }
}
