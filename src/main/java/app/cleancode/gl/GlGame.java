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
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import app.cleancode.game.Node;

public class GlGame {
    private static final float fov = (float) Math.toRadians(60.0);
    private static final float zNear = 0.1f;
    private static final float zFar = 1024;

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
        GL.createCapabilities();
        GL30.glClearColor(0.5294f, 0.8078f, 0.9216f, 0.0f);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

    private Matrix4f projectionMatrix;

    private void loop() {
        // Initialise the projection matrix
        projectionMatrix = new Matrix4f().perspective(fov,
                (float) vidmode.width() / vidmode.height(), zNear, zFar);

        ShaderProgram shaders = new ShaderProgram();
        shaders.bind();
        shaders.setUniform("projectionMatrix", projectionMatrix);

        GlObject triangle = new GlObject(new float[] {-0.5f, -0.5f, 0.0f, // Vertex 1
                -0.5f, 0.5f, 0.0f, // vertex 2
                0.5f, 0.5f, 0.0f, // Vertex 3
                0.5f, -0.5f, 0.0f, // Vertex 4
                -0.5f, -0.5f, 0.5f, // Vertex 5
                -0.5f, 0.5f, 0.5f, // vertex 6
                0.5f, 0.5f, 0.5f, // Vertex 7
                0.5f, -0.5f, 0.5f, // Vertex 8
        }, new float[] {0.0f, 0.0f, 0.0f, // Vertex 1
                1.0f, 0.0f, 0.0f, // Vertex 2
                1.0f, 1.0f, 1.0f, // Vertex 3
                0.0f, 0.0f, 1.0f, // Vertex 4
                0.0f, 0.0f, 0.0f, // Vertex 5
                1.0f, 0.0f, 0.0f, // Vertex 6
                1.0f, 1.0f, 1.0f, // Vertex 7
                0.0f, 0.0f, 1.0f, // Vertex 8
        }, new int[] {0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 5}, shaders);

        Node triangleNode = new Node(triangle);
        triangleNode.setTranslateZ(-2.0f);

        long lastTime = System.nanoTime();
        long frameDuration = 0;

        try {
            while (!glfwWindowShouldClose(window)) {
                frameDuration = System.nanoTime() - lastTime;
                lastTime = System.nanoTime();
                GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

                triangleNode.render();
                triangleNode.setRotateY(triangleNode.getRotateY() + 0.5f);
                gameLoopCallback.run();

                glfwSwapBuffers(window);

                glfwPollEvents();
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        triangleNode.cleanup();
        shaders.cleanup();
    }
}
