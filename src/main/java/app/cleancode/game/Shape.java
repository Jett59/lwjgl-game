package app.cleancode.game;

public class Shape {
    public final float[] vertices;
    public final float[] textureCoordinates;
    public final int[] indices;

    public Shape(float[] vertices, float[] textureCoordinates, int[] indices) {
        this.vertices = vertices;
        this.textureCoordinates = textureCoordinates;
        this.indices = indices;
    }
}
