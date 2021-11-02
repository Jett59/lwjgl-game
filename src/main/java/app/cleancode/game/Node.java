package app.cleancode.game;

import app.cleancode.gl.GlRenderer;

public class Node {
    private final GlRenderer renderer;

    public Node(float[] vertices, int[] indices) {
        renderer = new GlRenderer(vertices, indices);
    }

    public void render() {
        renderer.render();
    }

    public void cleanup() {
        renderer.cleanup();
    }
}
