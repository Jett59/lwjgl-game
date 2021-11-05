package app.cleancode.gl;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public class GlfwWindow implements AutoCloseable {
    private final long window;

    public GlfwWindow(String title, boolean resizable, long monitor, int x, int y, int width,
            int height, boolean vsync) {
        // Reset the window properties
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        // Create the window
        window = GLFW.glfwCreateWindow(width, height, title, monitor, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to initialise window " + title);
        }
        GLFW.glfwSetWindowPos(window, x, y);
        GLFW.glfwMakeContextCurrent(window);
        if (vsync) {
            GLFW.glfwSwapInterval(1);
        }
    }

    public long getWindowHandle() {
        return window;
    }

    public void setCursorEnabled(boolean enabled) {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
                enabled ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED);
    }

    public void show() {
        GLFW.glfwShowWindow(window);
    }

    @Override
    public void close() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

}
