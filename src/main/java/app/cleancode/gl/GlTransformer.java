package app.cleancode.gl;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class GlTransformer implements AutoCloseable {
    private Matrix4f transformationMatrix;
    private FloatBuffer matrixBuffer;
    private Vector3f translation;
    private float scale;
    private Vector3f rotation;
    private boolean changed = true;

    public GlTransformer() {
        transformationMatrix = new Matrix4f();
        matrixBuffer = MemoryUtil.memAllocFloat(16);
        translation = new Vector3f();
        scale = 1f;
        rotation = new Vector3f();
    }

    private void calculateMatrix() {
        if (changed) {
            transformationMatrix.translation(translation);
            transformationMatrix.rotateX((float) Math.toRadians(rotation.x));
            transformationMatrix.rotateY((float) Math.toRadians(rotation.y));
            transformationMatrix.rotateZ((float) Math.toRadians(rotation.z));
            transformationMatrix.scale(scale);
            changed = false;
        }
    }

    public void setTranslateX(float value) {
        translation.x = value;
    }

    public float getTranslateX() {
        return translation.x;
    }

    public void setTranslateY(float value) {
        translation.y = value;
    }

    public float getTranslateY() {
        return translation.y;
    }

    public void setTranslateZ(float value) {
        translation.z = value;
    }

    public float getTranslateZ() {
        return translation.z;
    }

    public void setScale(float value) {
        scale = value;
    }

    public float getScale() {
        return scale;
    }

    public void setRotateX(float value) {
        rotation.x = value;
    }

    public float getRotateX() {
        return rotation.x;
    }

    public void setRotateY(float value) {
        rotation.y = value;
    }

    public float getRotateY() {
        return rotation.y;
    }

    public void setRotateZ(float value) {
        rotation.z = value;
    }

    public float getRotateZ() {
        return rotation.z;
    }

    public void applyTransforms() {
        calculateMatrix();
    }

    public void applyCamera(GlCamera camera) {
        camera.getMatrix().mul(transformationMatrix, transformationMatrix);
        changed = true;
    }

    @Override
    public void close() {
        MemoryUtil.memFree(matrixBuffer);
    }

    protected FloatBuffer getMatrix() {
        return transformationMatrix.get(matrixBuffer);
    }
}
