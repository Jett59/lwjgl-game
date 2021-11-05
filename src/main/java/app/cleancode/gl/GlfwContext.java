package app.cleancode.gl;

import static org.lwjgl.glfw.GLFW.glfwInit;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class GlfwContext implements AutoCloseable {
    private final List<GlfwWindow> windows;

    public GlfwContext() {
        windows = new ArrayList<>();
        // Initialise glfw runtime;
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }

    public GlfwWindow addWindow(GlfwWindow window) {
        windows.add(window);
        return window;
    }

    @Override
    public void close() throws Exception {
        windows.forEach(GlfwWindow::close);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

}
