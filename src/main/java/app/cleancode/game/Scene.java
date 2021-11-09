package app.cleancode.game;

import java.util.LinkedList;
import java.util.List;
import app.cleancode.gl.GlContext;

public class Scene {
    private final List<Node> nodes;
    private final FrustumFilter frustumFilter;

    public Scene() {
        nodes = new LinkedList<>();
        frustumFilter = new FrustumFilter();
    }

    public Node add(Node node) {
        nodes.add(node);
        return node;
    }

    public void remove(Node node) {
        nodes.remove(node);
    }

    public void render(GlContext context) {
        if (context.getCamera().hasChanged()) {
            context.updateProjectionViewMatrix(context.getCamera().getMatrix());
            frustumFilter.update(context.getProjectionViewMatrix());
        }
        for (Node node : nodes) {
            if (node.shouldRender() && frustumFilter.shouldRender(node.getTranslateX(),
                    node.getTranslateY(), node.getTranslateZ(), node.getBoundingRadius())) {
                node.applyTransforms();
                node.applyCamera(context.getCamera());
                node.render();
            }
        }
    }
}
