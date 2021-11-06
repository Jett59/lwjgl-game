package app.cleancode.game;

import app.cleancode.gl.GlCamera;
import app.cleancode.gl.GlObject;
import app.cleancode.gl.GlTransformer;

public class Node implements AutoCloseable {
    private final GlObject glObject;
    private final GlTransformer transformer;

    public Node(GlObject glObject) {
        this.glObject = glObject;
        this.transformer = new GlTransformer();
    }

    public void render() {
        glObject.render(transformer);
    }

    public void close() {
        transformer.close();
    }

    public void setTranslateX(float value) {
        transformer.setTranslateX(value);
    }

    public float getTranslateX() {
        return transformer.getTranslateX();
    }

    public void setTranslateY(float value) {
        transformer.setTranslateY(value);
    }

    public float getTranslateY() {
        return transformer.getTranslateY();
    }

    public void setTranslateZ(float value) {
        transformer.setTranslateZ(value);
    }

    public float getTranslateZ() {
        return transformer.getTranslateZ();
    }

    public void setScale(float value) {
        transformer.setScale(value);
    }

    public void setRotateX(float value) {
        transformer.setRotateX(value);
    }

    public float getRotateX() {
        return transformer.getRotateX();
    }

    public void setRotateY(float value) {
        transformer.setRotateY(value);
    }

    public float getRotateY() {
        return transformer.getRotateY();
    }

    public void setRotateZ(float value) {
        transformer.setRotateZ(value);
    }

    public float getRotateZ() {
        return transformer.getRotateZ();
    }

    public void applyTransforms() {
        transformer.applyTransforms();
    }

    public void applyCamera(GlCamera camera) {
        transformer.applyCamera(camera);
    }
}
