package app.cleancode;

import org.lwjgl.glfw.GLFW;
import app.cleancode.game.GameLogic;
import app.cleancode.game.Scene;
import app.cleancode.game.World;
import app.cleancode.game.block.Blocks;
import app.cleancode.game.player.Player;
import app.cleancode.game.terrain.TerrainGenerator;
import app.cleancode.gl.GlCamera;
import app.cleancode.gl.GlContext;
import app.cleancode.gl.GlGame;
import app.cleancode.gl.GlfwWindow;
import app.cleancode.profiler.Profiler;

public class Entrypoint implements GameLogic {
    private static final float speed = 0.02f;
    private static final double mouseSensitivity = 0.1;

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        boolean shouldProfile = false;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--profile")) {
                shouldProfile = true;
            }
        }
        Profiler profiler = null;
        Thread profilerThread = null;
        if (shouldProfile) {
            System.out.println("Starting profiler");
            profiler = new Profiler();
            profilerThread = new Thread(profiler, "Profiler thread");
            profilerThread.setDaemon(true);
            profilerThread.start();
        }
        GlGame game = new GlGame(new Entrypoint());
        game.run();
        if (shouldProfile) {
            profilerThread.stop();
            profiler.dump();
        }
    }

    private final Scene scene = new Scene();
    private final World world = new World();
    private final TerrainGenerator terrainGenerator = new TerrainGenerator();
    private Player player;

    @Override
    public void begin(GlContext context) { // TODO Auto-generated method stub
        Blocks.initBlocks(context);
        terrainGenerator.generateTerrain(scene, world);
        player = new Player(context.getCamera());
        player.move(0, 10, 0, world);
    }

    private double previousMouseX = Double.MAX_VALUE, previousMouseY = Double.MAX_VALUE;

    private long lastMeasurementTimeMillis = Long.MIN_VALUE;
    private long frames = 0;
    private long fps = 0;

    @Override
    public void update(GlContext context, GlfwWindow window) {
        frames++;
        if (frames >= 100) {
            long currentTimeMillis = System.currentTimeMillis();
            fps = (long) (1000d
                    / ((currentTimeMillis - lastMeasurementTimeMillis) / (double) frames));
            lastMeasurementTimeMillis = currentTimeMillis;
            window.setTitle(String.format("Game - %d fps", fps));
            frames = 0;
        }
        scene.render(context);
        GlCamera camera = context.getCamera();
        if (previousMouseX != Double.MAX_VALUE && previousMouseY != Double.MAX_VALUE) {
            double mouseX = window.getMouseX();
            double mouseY = window.getMouseY();
            if (mouseX != previousMouseX || mouseY != previousMouseY) {
                camera.rotate((float) ((mouseY - previousMouseY) * mouseSensitivity),
                        (float) ((mouseX - previousMouseX) * mouseSensitivity), 0);
            }
        }
        previousMouseX = window.getMouseX();
        previousMouseY = window.getMouseY();
        if (window.isKeyDown(GLFW.GLFW_KEY_W)) {
            player.move(0, 0, -speed, world);
        } else if (window.isKeyDown(GLFW.GLFW_KEY_S)) {
            player.move(0, 0, speed, world);
        }
        if (window.isKeyDown(GLFW.GLFW_KEY_A)) {
            player.move(-speed, 0, 0, world);
        } else if (window.isKeyDown(GLFW.GLFW_KEY_D)) {
            player.move(speed, 0, 0, world);
        }
        if (window.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            player.move(0, speed, 0, world);
        } else if (window.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            player.move(0, -speed, 0, world);
        }
    }
}
