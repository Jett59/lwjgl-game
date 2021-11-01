package app.cleancode.gl;

import org.lwjgl.opengl.GL30;

public enum GlFaceType {
    SQUARE(GL30.GL_QUADS), TRIANGLE(GL30.GL_TRIANGLES);

    protected int glFaceType;

    private GlFaceType(int glFaceType) {
        this.glFaceType = glFaceType;
    }
}
