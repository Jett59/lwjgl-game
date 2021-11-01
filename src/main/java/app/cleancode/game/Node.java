package app.cleancode.game;

import app.cleancode.gl.GlFaceType;
import app.cleancode.gl.GlRenderer;

public class Node {
    private final GlRenderer renderer;

    public Node(float[] vertices, GlFaceType faceType) {
        renderer = new GlRenderer(vertices, faceType);
    }

    public void render() {
        renderer.render();
    }

    public void cleanup() {
        renderer.cleanup();
    }
}
