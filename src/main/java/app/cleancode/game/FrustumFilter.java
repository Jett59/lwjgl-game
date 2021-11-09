package app.cleancode.game;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class FrustumFilter {
    private static final int planes = 6;

    private Vector4f[] frustums = new Vector4f[planes];

    public FrustumFilter() {
        for (int i = 0; i < planes; i++) {
            frustums[i] = new Vector4f();
        }
    }

    public void update(Matrix4f projectionViewMatrix) {
        for (int i = 0; i < planes; i++) {
            projectionViewMatrix.frustumPlane(i, frustums[i]);
        }
    }

    public boolean shouldRender(float x, float y, float z, float boundingRadius) {
        boolean result = true;
        for (int i = 0; i < planes; i++) {
            Vector4f plane = frustums[i];
            if (plane.x * x + plane.y * y + plane.z * z + plane.w <= -boundingRadius) {
                result = false;
                break;
            }
        }
        return result;
    }
}
