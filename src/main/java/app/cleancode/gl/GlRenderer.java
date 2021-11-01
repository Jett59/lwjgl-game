package app.cleancode.gl;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class GlRenderer {
    private final int vaoId;
    private final int vboId;
    private final GlFaceType faceType;
    private final int numVertices;

    public GlRenderer(float[] vertices, GlFaceType faceType) {
        if (vertices.length % 3 != 0) {
            throw new IllegalArgumentException(
                    "Length of vertices not a multiple of 3: " + vertices.length);
        }
        this.faceType = faceType;
        this.numVertices = vertices.length / 3;
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        vboId = GL30.glGenBuffers();
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        try {
            vertexBuffer.put(vertices).flip();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(0);
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
        } finally {
            MemoryUtil.memFree(vertexBuffer);
        }
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render() {
        GL30.glBindVertexArray(vaoId);
        GL30.glDrawArrays(faceType.glFaceType, 0, numVertices);
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL30.glDeleteVertexArrays(vaoId);
        GL30.glDeleteBuffers(vboId);
    }
}
