package app.cleancode.game;

import app.cleancode.gl.GlContext;
import app.cleancode.gl.GlfwWindow;

public interface GameLogic {
    void begin(GlContext context);

    void update(GlContext context, GlfwWindow window);
}
