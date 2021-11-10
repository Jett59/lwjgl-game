package app.cleancode;

import org.lwjgl.glfw.GLFW;
import app.cleancode.game.GameLogic;
import app.cleancode.game.Scene;
import app.cleancode.game.World;
import app.cleancode.game.block.Blocks;
import app.cleancode.game.player.Player;
import app.cleancode.game.terrain.TerrainGenerator;
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
        player.move(0, world.surfaceLevelOf(0, 0), 0, world);
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
        if (previousMouseX != Double.MAX_VALUE && previousMouseY != Double.MAX_VALUE) {
            double mouseX = window.getMouseX();
            double mouseY = window.getMouseY();
            if (mouseX != previousMouseX || mouseY != previousMouseY) {
                player.rotate((float) ((mouseY - previousMouseY) * mouseSensitivity),
                        (float) ((mouseX - previousMouseX) * mouseSensitivity));
            }
        }
        previousMouseX = window.getMouseX();
        previousMouseY = window.getMouseY();
        if (window.isKeyDown(GLFW.GLFW_KEY_W)) {
            player.zVelocity = Math.min(-0.06f, -player.zVelocity - speed);
        } else if (window.isKeyDown(GLFW.GLFW_KEY_S)) {
            player.zVelocity = Math.min(0.06f, player.zVelocity + speed);
        } else {
            player.zVelocity = 0;
        }
        if (window.isKeyDown(GLFW.GLFW_KEY_A)) {
            player.xVelocity = Math.max(-0.06f, -player.xVelocity - speed);
        } else if (window.isKeyDown(GLFW.GLFW_KEY_D)) {
            player.xVelocity = Math.min(0.06f, player.xVelocity + speed);
        } else {
            player.xVelocity = 0;
        }
        if (window.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            if (player.isTouchingGround(world, -player.yVelocity)) {
                player.yVelocity = speed * 2.5f;
            }
        }
        if (!player.isTouchingGround(world, -player.yVelocity)) {
            player.yVelocity -= speed / 24;
        } else {
            if (player.yVelocity < 0) {
                player.yVelocity = 0;
            }
        }
        player.move(player.xVelocity, player.yVelocity, player.zVelocity, world);
    }
}
