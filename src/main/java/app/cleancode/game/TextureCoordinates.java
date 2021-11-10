package app.cleancode.game;

public class TextureCoordinates {
    public static final float[] topBottomSide = { // Texture coordinates for the faces of a typical
                                                  // (top, bottom, sides) texture
            // Face 1: front
            1, 0.5f, // Middle-right
            0.5f, 0.5f, // Middle-middle
            0.5f, 0, // Top-middle
            1, 0, // Top-right
            // Face 2: back
            1, 0.5f, // Middle-right
            0.5f, 0.5f, // Middle-middle
            0.5f, 0, // Top-middle
            1, 0, // Top-right
            // Face 3: left
            1, 0.5f, // Middle-right
            0.5f, 0.5f, // Middle-middle
            0.5f, 0, // Top-middle
            1, 0, // Top-right
            // Face 4: right
            1, 0.5f, // Middle-right
            0.5f, 0.5f, // Middle-middle
            0.5f, 0, // Top-middle
            1, 0, // Top-right
            // Face 5: top
            0, 0, // Top-left
            0, 0.5f, // middle-left
            0.5f, 0.5f, // Middle-middle
            0.5f, 0, // Top-middle
            // Face 6: bottom
            0, 0.5f, // Middle-left
            0, 1, // Bottom-left
            0.5f, 1, // Bottom-middle
            0.5f, 0.5f, // Middle-middle
    };
    public static final float[] faces = { // Texture coordinates for single face blocks
            // Face 1: front
            0, 0, // top-left
            1, 0, // top-right
            1, 1, // bottom-right
            0, 1, // bottom-left
            // Face 2: back
            0, 0, // top-left
            1, 0, // top-right
            1, 1, // bottom-right
            0, 1, // bottom-left
            // Face 3: left
            0, 0, // top-left
            1, 0, // top-right
            1, 1, // bottom-right
            0, 1, // bottom-left
            // Face 4: right
            0, 0, // top-left
            1, 0, // top-right
            1, 1, // bottom-right
            0, 1, // bottom-left
            // Face 5: top
            0, 0, // top-left
            1, 0, // top-right
            1, 1, // bottom-right
            0, 1, // bottom-left
            // Face 6: bottom
            0, 0, // top-left
            1, 0, // top-right
            1, 1, // bottom-right
            0, 1, // bottom-left
    };
}
