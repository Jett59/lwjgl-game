package app.cleancode.gl;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GlTransformer {
    private Matrix4f transformationMatrix;
    private Vector3f translation;
    private float scale;
    private Vector3f rotation;

    public GlTransformer() {
        transformationMatrix = new Matrix4f();
        translation = new Vector3f();
        scale = 1f;
        rotation = new Vector3f();
        calculateMatrix();
    }

    private void calculateMatrix() {
        transformationMatrix.identity();
        transformationMatrix.translate(translation);
        transformationMatrix.scale(scale);
        transformationMatrix.rotateX((float) Math.toRadians(rotation.x));
        transformationMatrix.rotateY((float) Math.toRadians(rotation.y));
        transformationMatrix.rotateZ((float) Math.toRadians(rotation.z));
    }

    public void setTranslateX(float value) {
        translation.x = value;
        calculateMatrix();
    }

    public float getTranslateX() {
        return translation.x;
    }

    public void setTranslateY(float value) {
        translation.y = value;
        calculateMatrix();
    }

    public float getTranslateY() {
        return translation.y;
    }

    public void setTranslateZ(float value) {
        translation.z = value;
        calculateMatrix();
    }

    public float getTranslateZ() {
        return translation.z;
    }

    public void setScale(float value) {
        scale = value;
        calculateMatrix();
    }

    public float getScale() {
        return scale;
    }

    public void setRotateX(float value) {
        rotation.x = value;
        calculateMatrix();
    }

    public float getRotateX() {
        return rotation.x;
    }

    public void setRotateY(float value) {
        rotation.y = value;
        calculateMatrix();
    }

    public float getRotateY() {
        return rotation.y;
    }

    public void setRotateZ(float value) {
        rotation.z = value;
        calculateMatrix();
    }

    public float getRotateZ() {
        return rotation.z;
    }

    protected Matrix4f getMatrix() {
        return transformationMatrix;
    }
}
