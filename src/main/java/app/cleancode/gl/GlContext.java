package app.cleancode.gl;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public class GlContext implements AutoCloseable {
    private final List<AutoCloseable> objects;
    private ShaderProgram shaders;
    private GlCamera camera;
    private final Matrix4f projectionMatrix;
    private final Matrix4f projectionViewMatrix;

    public GlContext() {
        objects = new ArrayList<>();
        projectionMatrix = new Matrix4f();
        projectionViewMatrix = new Matrix4f();
    }

    protected void setShaders(ShaderProgram shaders) {
        this.shaders = shaders;
    }

    public ShaderProgram getShaders() {
        return shaders;
    }

    protected void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix.set(projectionMatrix);
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

    public Matrix4f updateProjectionViewMatrix(Matrix4f viewMatrix) {
        projectionMatrix.mul(viewMatrix, projectionViewMatrix);
        return getProjectionViewMatrix();
    }

    public Matrix4f getProjectionViewMatrix() {
        return projectionViewMatrix;
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
