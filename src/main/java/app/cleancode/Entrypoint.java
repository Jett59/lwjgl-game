package app.cleancode;

import org.lwjgl.glfw.GLFW;
import app.cleancode.game.GameLogic;
import app.cleancode.game.Scene;
import app.cleancode.game.World;
import app.cleancode.game.block.Block;
import app.cleancode.game.block.BlockIds;
import app.cleancode.game.block.BlockPosition;
import app.cleancode.game.block.Blocks;
import app.cleancode.gl.GlCamera;
import app.cleancode.gl.GlContext;
import app.cleancode.gl.GlGame;
import app.cleancode.gl.GlfwWindow;

public class Entrypoint implements GameLogic {
    private static final float speed = 0.02f;
    private static final double mouseSensitivity = 0.2;

    public static void main(String[] args) {
        GlGame game = new GlGame(new Entrypoint());
        game.run();
    }

    private final Scene scene = new Scene();
    private final World world = new World();

    @Override
    public void begin(GlContext context) { // TODO Auto-generated method stub
        Blocks.initBlocks(context);
        for (int x = -50; x < 50; x++) {
            for (int z = -50; z < 50; z++) {
                scene.add(new Block(BlockIds.block, world, new BlockPosition(x, -2, z)));
            }
        }
    }

    private double previousMouseX = Double.MAX_VALUE, previousMouseY = Double.MAX_VALUE;

    @Override
    public void update(GlContext context, GlfwWindow window) {
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
}
