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
import app.cleancode.game.GameLogic;

public class GlGame {
    private static final float fieldOfView = (float) Math.toRadians(60.0);
    private static final float zNear = 0.1f;
    private static final float zFar = 1024;

    private GlfwWindow window;
    private GameLogic gameLogic;
    private long primaryMonitor;
    private int screenWidth, screenHeight;

    public GlGame(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
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

        context.setShaders(shaders);

        GlCamera camera = new GlCamera();
        context.setCamera(camera);

        DoubleBuffer cursorX = MemoryUtil.memAllocDouble(1);
        DoubleBuffer cursorY = MemoryUtil.memAllocDouble(1);

        try {
            gameLogic.begin(context);
            while (!glfwWindowShouldClose(window.getWindowHandle())) {
                GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

                cursorX.position(0);
                cursorY.position(0);
                GLFW.glfwGetCursorPos(window.getWindowHandle(), cursorX, cursorY);
                window.setMousePosition(cursorX.get(0), cursorY.get(0));

                gameLogic.update(context, window);

                glfwSwapBuffers(window.getWindowHandle());

                glfwPollEvents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MemoryUtil.memFree(cursorY);
        MemoryUtil.memFree(cursorX);
    }
}
