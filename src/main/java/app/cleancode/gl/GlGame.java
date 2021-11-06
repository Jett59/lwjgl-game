package app.cleancode.gl;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import java.nio.DoubleBuffer;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
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

    private GlfwWindow window;
    private Runnable gameLoopCallback;
    private long primaryMonitor;
    private int screenWidth, screenHeight;

    public GlGame(Runnable gameLoopCallback) {
        this.gameLoopCallback = gameLoopCallback;
    }

    public void run() {
        try (GlfwContext glfwContext = new GlfwContext(); GlContext glContext = new GlContext()) {
            init(glfwContext);
            loop(glContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(GlfwContext context) {
        primaryMonitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidmode = glfwGetVideoMode(primaryMonitor);
        screenWidth = vidmode.width();
        screenHeight = vidmode.height();
        window = context.addWindow(new GlfwWindow("Game", false, primaryMonitor, 0, 0, screenWidth,
                screenHeight, true));
        window.setCursorEnabled(false);
        window.show();
        GL.createCapabilities();
        GL30.glClearColor(0.5294f, 0.8078f, 0.9216f, 0.0f);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

    private Matrix4f projectionMatrix;

    private void loop(GlContext context) {
        // Initialise the projection matrix
        projectionMatrix = new Matrix4f().perspective(fieldOfView,
                (float) screenWidth / screenHeight, zNear, zFar);

        ShaderProgram shaders = context.addObject(new ShaderProgram());
        shaders.bind();
        shaders.setUniform("projectionMatrix", projectionMatrix);

        Box triangleBox = new Box(-0.5f, -0.5f, -0.5f, 1, 1, 1);

        GlObject triangle =
                context.addObject(new GlObject(triangleBox, shaders, new GlTexture("cube")));

        Node triangleNode = context.addObject(new Node(triangle));
        triangleNode.setTranslateZ(-2.0f);

        GlCamera camera = new GlCamera();

        DoubleBuffer cursorX = MemoryUtil.memAllocDouble(1);
        DoubleBuffer cursorY = MemoryUtil.memAllocDouble(1);

        double previousCursorX = Double.POSITIVE_INFINITY,
                previousCursorY = Double.POSITIVE_INFINITY;

        long lastTime = System.nanoTime();
        long frameDuration = 0;

        try {
            while (!glfwWindowShouldClose(window.getWindowHandle())) {
                frameDuration = System.nanoTime() - lastTime;
                lastTime = System.nanoTime();
                GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

                triangleNode.applyTransforms();
                triangleNode.applyCamera(camera);
                triangleNode.render();
                gameLoopCallback.run();

                glfwSwapBuffers(window.getWindowHandle());

                glfwPollEvents();
                cursorX.position(0);
                cursorY.position(0);
                GLFW.glfwGetCursorPos(window.getWindowHandle(), cursorX, cursorY);
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
                if (window.isKeyDown(GLFW.GLFW_KEY_W)) {
                    camera.move(0, 0, -speed);
                } else if (window.isKeyDown(GLFW.GLFW_KEY_S)) {
                    camera.move(0, 0, speed);
                }
                if (window.isKeyDown(GLFW.GLFW_KEY_A)) {
                    camera.move(-speed, 0, 0);
                } else if (window.isKeyDown(GLFW.GLFW_KEY_D)) {
                    camera.move(speed, 0, 0);
                }
                if (window.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
                    camera.move(0, speed, 0);
                } else if (window.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                    camera.move(0, -speed, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MemoryUtil.memFree(cursorY);
        MemoryUtil.memFree(cursorX);
    }
}
