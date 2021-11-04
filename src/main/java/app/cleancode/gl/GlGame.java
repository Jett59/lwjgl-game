package app.cleancode.gl;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;
import java.nio.DoubleBuffer;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import app.cleancode.game.Box;
import app.cleancode.game.Node;

public class GlGame {
    private static final float fieldOfView = (float) Math.toRadians(60.0);
    private static final float zNear = 0.1f;
    private static final float zFar = 1024;
    private static final float mouseSensitivity = 0.2f;
    private static final float speed = 0.02f;

    private long window;
    private Runnable gameLoopCallback;
    private GLFWVidMode vidmode;

    public GlGame(Runnable gameLoopCallback) {
        this.gameLoopCallback = gameLoopCallback;
    }

    public void run() {
        init();
        loop();

        // Clean up
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Create the window
        window = glfwCreateWindow(vidmode.width(), vidmode.height(), "Game",
                glfwGetPrimaryMonitor(), NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        GLFW.glfwSetWindowPos(window, 0, 0);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        GL.createCapabilities();
        GL30.glClearColor(0.5294f, 0.8078f, 0.9216f, 0.0f);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

    private Matrix4f projectionMatrix;

    private boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    private void loop() {
        // Initialise the projection matrix
        projectionMatrix = new Matrix4f().perspective(fieldOfView,
                (float) vidmode.width() / vidmode.height(), zNear, zFar);

        ShaderProgram shaders = new ShaderProgram();
        shaders.bind();
        shaders.setUniform("projectionMatrix", projectionMatrix);

        Box triangleBox = new Box(-0.5f, -0.5f, -0.5f, 1, 1, 1);

        GlObject triangle = new GlObject(triangleBox, shaders, new GlTexture("cube"));

        Node triangleNode = new Node(triangle);
        triangleNode.setTranslateZ(-2.0f);

        GlCamera camera = new GlCamera();

        DoubleBuffer cursorX = MemoryUtil.memAllocDouble(1);
        DoubleBuffer cursorY = MemoryUtil.memAllocDouble(1);

        double previousCursorX = Double.POSITIVE_INFINITY,
                previousCursorY = Double.POSITIVE_INFINITY;

        long lastTime = System.nanoTime();
        long frameDuration = 0;

        try {
            while (!glfwWindowShouldClose(window)) {
                frameDuration = System.nanoTime() - lastTime;
                lastTime = System.nanoTime();
                GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

                triangleNode.applyCamera(camera);
                triangleNode.render();
                gameLoopCallback.run();

                glfwSwapBuffers(window);

                glfwPollEvents();
                cursorX.position(0);
                cursorY.position(0);
                GLFW.glfwGetCursorPos(window, cursorX, cursorY);
                double newCursorX = cursorX.get(0);
                double newCursorY = cursorY.get(0);
                if (previousCursorX == Double.POSITIVE_INFINITY
                        || previousCursorY == Double.POSITIVE_INFINITY) {
                    previousCursorX = newCursorX;
                    previousCursorY = newCursorY;
                }
                camera.rotate((float) (newCursorY - previousCursorY) * mouseSensitivity,
                        (float) (newCursorX - previousCursorX) * mouseSensitivity, 0);
                previousCursorX = newCursorX;
                previousCursorY = newCursorY;
                if (isKeyDown(GLFW.GLFW_KEY_W)) {
                    camera.move(0, 0, -speed);
                } else if (isKeyDown(GLFW.GLFW_KEY_S)) {
                    camera.move(0, 0, speed);
                }
                if (isKeyDown(GLFW.GLFW_KEY_A)) {
                    camera.move(-speed, 0, 0);
                } else if (isKeyDown(GLFW.GLFW_KEY_D)) {
                    camera.move(speed, 0, 0);
                }
                if (isKeyDown(GLFW.GLFW_KEY_SPACE)) {
                    camera.move(0, speed, 0);
                } else if (isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                    camera.move(0, -speed, 0);
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        MemoryUtil.memFree(cursorY);
        MemoryUtil.memFree(cursorX);
        triangleNode.cleanup();
        shaders.cleanup();
    }
}
