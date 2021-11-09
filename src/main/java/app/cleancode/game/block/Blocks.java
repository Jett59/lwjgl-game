package app.cleancode.game.block;

import java.util.HashMap;
import java.util.Map;
import app.cleancode.game.Box;
import app.cleancode.gl.GlContext;
import app.cleancode.gl.GlObject;
import app.cleancode.gl.GlTexture;

public class Blocks {
    private static final Map<Integer, GlObject> blocks = new HashMap<>();

    public static GlObject getBlock(int id) {
        return blocks.get(id);
    }

    private static void registerBlock(GlContext context, int id, String textureName) {
        blocks.put(id,
                context.addObject(
                        new GlObject(new Box(-0.5f, -0.5f, -0.5f, 1f, 1f, 1f), context.getShaders(),
                                new GlTexture(textureName), 0.87f /* Math.sqrt(0.5 * 0.5 * 3) */)));
    }

    public static void initBlocks(GlContext context) {
        registerBlock(context, BlockIds.grass, "grass");
    }
}
