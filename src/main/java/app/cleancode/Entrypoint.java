package app.cleancode;

import app.cleancode.gl.GlGame;

public class Entrypoint {
    public static void gameLoopCallback() {
        return;
    }

    public static void main(String[] args) {
        GlGame game = new GlGame(Entrypoint::gameLoopCallback);
        game.run();
    }
}
