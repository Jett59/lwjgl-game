package app.cleancode.game;

public class Box extends Shape {
    private static float[] genVertices(float x, float y, float z, float width, float height,
            float depth) {
        return new float[] { // 4 vertices per face
                // Face 1: front
                x, y, z + depth, // Verticy 0
                x + width, y, z + depth, // Verticy 1
                x + width, y + height, z + depth, // Verticy 2
                x, y + height, z + depth, // Verticy 3
                // Face 2: back
                x, y, z, // Verticy 4
                x + width, y, z, // Verticy 5
                x + width, y + height, z, // Verticy 6
                x, y + height, z, // Verticy 7
                // Face 3: left
                x, y, z, // Verticy 8
                x, y, z + depth, // Verticy 9
                x, y + height, z + depth, // Verticy 10
                x, y + height, z, // Verticy 11
                // Face 4: right
                x + width, y, z, // Verticy 12
                x + width, y, z + depth, // Verticy 13
                x + width, y + height, z + depth, // Verticy 14
                x + width, y + height, z, // Verticy 15
                // Face 5: top
                x, y + height, z, // Verticy 16
                x + width, y + height, z, // Verticy 17
                x + width, y + height, z + depth, // Verticy 18
                x, y + height, z + depth, // Verticy 19
                // Face 6: bottom
                x, y, z, // Verticy 20
                x + width, y, z, // Verticy 21
                x + width, y, z + depth, // Verticy 22
                x, y, z + depth, // Verticy 23
        };
    }

    private static final int[] defaultIndices = { // 36: 3 for each triangle
            // Face 1: front
            0, 1, 2, 2, 3, 0, // 2 triangles
            // Face 2: back
            4, 5, 6, 6, 7, 4, // 2 triangles
            // Face 3: left
            8, 9, 10, 10, 11, 8, // 2 triangles
            // Face 4: right
            12, 13, 14, 14, 15, 12, // 2 triangles
            // Face 5: top
            16, 17, 18, 18, 19, 16, // 2 triangles
            // Face 6: bottom
            20, 21, 22, 22, 23, 20, // 2 triangles
    };

    public Box(float x, float y, float z, float width, float height, float depth,
            float[] textureCoordinates) {
        super(genVertices(x, y, z, width, height, depth), textureCoordinates, defaultIndices);
    }

}
