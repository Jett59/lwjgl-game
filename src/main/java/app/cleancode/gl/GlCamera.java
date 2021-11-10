package app.cleancode.gl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GlCamera {
    private final Matrix4f matrix;
    private Vector3f translation;
    private final Vector2f rotation;
    private boolean changed;

    public GlCamera() {
        matrix = new Matrix4f();
        translation = new Vector3f();
        rotation = new Vector2f();
        changed = true;
    }

    public void move(float x, float y, float z) {
        translation.x = x;
        translation.y = y;
        translation.z = z;
        changed = true;
    }

    public void rotate(float deltaX, float deltaY) {
        rotation.x += deltaX;
        rotation.y += deltaY;
        rotation.x %= 360f;
        rotation.y %= 360f;
        changed = true;
    }

    public Matrix4f getMatrix() {
        if (changed) {
            changed = false;
            matrix.identity();
            return matrix.rotateX((float) Math.toRadians(rotation.x))
                    .rotateY((float) Math.toRadians(rotation.y))
                    .translate(-translation.x, -translation.y, -translation.z);
        } else {
            return matrix;
        }
    }

    public boolean hasChanged() {
        return changed;
    }
}
