package app.cleancode.game;

import java.util.ArrayList;
import java.util.List;
import app.cleancode.gl.GlContext;

public class Scene {
    private final List<Node> nodes;

    public Scene() {
        nodes = new ArrayList<>();
    }

    public Node add(Node node) {
        nodes.add(node);
        return node;
    }

    public void remove(Node node) {
        nodes.remove(node);
    }

    public void render(GlContext context) {
        for (Node node : nodes) {
            if (node.shouldRender()) {
                node.applyTransforms();
                node.applyCamera(context.getCamera());
                node.render();
            }
        }
    }
}
