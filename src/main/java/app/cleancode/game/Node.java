package app.cleancode.game;

import app.cleancode.gl.GlObject;

public class Node {
    private final GlObject glObject;

    public Node(GlObject glObject) {
        this.glObject = glObject.createRef();
    }

    public void render() {
        glObject.render();
    }

    public void cleanup() {
        glObject.cleanup();
    }
}
