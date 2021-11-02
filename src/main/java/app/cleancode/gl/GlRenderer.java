package app.cleancode.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class GlRenderer {
    private final int vaoId;
    private final int vertexVboId;
    private final int indexVboId;
    private final int numVertices;

    public GlRenderer(float[] vertices, int[] indices) {
        if (vertices.length % 3 != 0) {
            throw new IllegalArgumentException(
                    "Length of vertices not a multiple of 3: " + vertices.length);
        }
        this.numVertices = indices.length;
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        vertexVboId = GL30.glGenBuffers();
        indexVboId = GL30.glGenBuffers();
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        try {
            vertexBuffer.put(vertices).flip();
            indexBuffer.put(indices).flip();
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexVboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(0);
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
            GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL30.GL_STATIC_DRAW);
        } finally {
            MemoryUtil.memFree(vertexBuffer);
            MemoryUtil.memFree(indexBuffer);
        }
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render() {
        GL30.glBindVertexArray(vaoId);
        GL30.glDrawElements(GL30.GL_TRIANGLES, numVertices, GL30.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL30.glDeleteVertexArrays(vaoId);
        GL30.glDeleteBuffers(vertexVboId);
        GL30.glDeleteBuffers(indexVboId);
    }
}
