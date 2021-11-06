package app.cleancode.gl;

import java.util.ArrayList;
import java.util.List;

public class GlContext implements AutoCloseable {
    private final List<AutoCloseable> objects;
    private ShaderProgram shaders;
    private GlCamera camera;

    public GlContext() {
        objects = new ArrayList<>();
    }

    protected void setShaders(ShaderProgram shaders) {
        this.shaders = shaders;
    }

    public ShaderProgram getShaders() {
        return shaders;
    }

    public void setCamera(GlCamera camera) {
        this.camera = camera;
    }

    public GlCamera getCamera() {
        return camera;
    }

    public <T extends AutoCloseable> T addObject(T object) {
        objects.add(object);
        return object;
    }

    @Override
    public void close() throws Exception {
        objects.forEach(object -> {
            try {
                object.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
