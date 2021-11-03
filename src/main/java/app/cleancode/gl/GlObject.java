package app.cleancode.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import app.cleancode.game.Shape;

public class GlObject {
    private final int vaoId;
    private final int vertexVboId;
    private final int textureVboId;
    private final int indexVboId;
    private final int numVertices;
    private int refs;
    private final ShaderProgram shaderProgram;
    private final GlTexture texture;

    public GlObject(Shape shape, ShaderProgram shaderProgram, GlTexture texture) {
        this(shape.vertices, shape.textureCoordinates, shape.indices, shaderProgram, texture);
    }

    public GlObject(float[] vertices, float[] textureCoords, int[] indices,
            ShaderProgram shaderProgram, GlTexture texture) {
        if (vertices.length % 3 != 0) {
            throw new IllegalArgumentException(
                    "Length of vertices not a multiple of 3: " + vertices.length);
        }
        if (textureCoords.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Number of texture coordinates not a multiple of 2: " + textureCoords.length);
        }
        if (textureCoords.length / 2 * 3 != vertices.length) {
            throw new IllegalArgumentException("Wrong number of colors for " + vertices.length / 3
                    + " vertices: " + textureCoords.length / 2);
        }
        this.numVertices = indices.length;
        this.shaderProgram = shaderProgram;
        this.texture = texture;
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        vertexVboId = GL30.glGenBuffers();
        textureVboId = GL30.glGenBuffers();
        indexVboId = GL30.glGenBuffers();
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
        try {
            vertexBuffer.put(vertices).flip();
            textureBuffer.put(textureCoords).flip();
            indexBuffer.put(indices).flip();
            // Vertex vbo
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vertexVboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(0);
            GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
            // texture coordinate vbo
            GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, textureVboId);
            GL30.glBufferData(GL30.GL_ARRAY_BUFFER, textureBuffer, GL30.GL_STATIC_DRAW);
            GL30.glEnableVertexAttribArray(1);
            GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
            // Index vbo
            GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL30.GL_STATIC_DRAW);
        } finally {
            MemoryUtil.memFree(vertexBuffer);
            MemoryUtil.memFree(textureBuffer);
            MemoryUtil.memFree(indexBuffer);
        }
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render(GlTransformer transformer) {
        shaderProgram.setUniform("transformationMatrix", transformer.getMatrix());
        shaderProgram.setUniform("textureUnit", 0);;
        texture.bind();
        GL30.glBindVertexArray(vaoId);
        GL30.glDrawElements(GL30.GL_TRIANGLES, numVertices, GL30.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
    }

    public GlObject createRef() {
        refs++;
        return this;
    }

    public void cleanup() {
        if (--refs == 0) {
            GL30.glDeleteVertexArrays(vaoId);
            GL30.glDeleteBuffers(vertexVboId);
            GL30.glDeleteBuffers(textureVboId);
            GL30.glDeleteBuffers(indexVboId);
            texture.cleanup();
        }
    }
}
