package app.cleancode.gl;

import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import app.cleancode.resources.ResourceReader;

public class ShaderProgram {
    private int program;
    private Map<String, Integer> shaderUniforms = new HashMap<>();

    public ShaderProgram() {
        program = GL30.glCreateProgram();
        if (program == 0) {
            throw new RuntimeException("Failed to create the program");
        }
        int vertexShader = compileShader("vertex", GL30.GL_VERTEX_SHADER);
        int fragmentShader = compileShader("fragment", GL30.GL_FRAGMENT_SHADER);
        GL30.glAttachShader(program, vertexShader);
        GL30.glAttachShader(program, fragmentShader);
        GL30.glLinkProgram(program);
        if (GL30.glGetProgrami(program, GL30.GL_LINK_STATUS) == 0) {
            throw new RuntimeException(
                    "Failed to link the shader program: " + GL30.glGetProgramInfoLog(program));
        }
        GL30.glDetachShader(program, vertexShader);
        GL30.glDetachShader(program, fragmentShader);
        GL30.glDeleteShader(vertexShader);
        GL30.glDeleteShader(fragmentShader);
        GL30.glValidateProgram(program);
        if (GL30.glGetProgrami(program, GL30.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning: while validating shader code:\n"
                    + GL30.glGetProgramInfoLog(program, 1024));
        }
    }

    private int compileShader(String name, int type) {
        try {
            byte[] shaderSource =
                    ResourceReader.readResource(String.format("/shaders/%s.glsl", name));
            int shader = GL30.glCreateShader(type);
            GL30.glShaderSource(shader, new String(shaderSource, StandardCharsets.UTF_8));
            GL30.glCompileShader(shader);
            if (GL30.glGetShaderi(shader, GL30.GL_COMPILE_STATUS) == 0) {
                throw new RuntimeException("Failed to compile shader " + name + "\nMessage: "
                        + GL30.glGetShaderInfoLog(shader));
            }
            return shader;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getUniformLocation(String name) {
        int uniformLocation;
        if (!shaderUniforms.containsKey(name)) {
            uniformLocation = GL30.glGetUniformLocation(program, name);
            if (uniformLocation < 0) {
                throw new IllegalArgumentException("Uniform " + name + " does not exist");
            }
            shaderUniforms.put(name, uniformLocation);
        } else {
            uniformLocation = shaderUniforms.get(name);
        }
        return uniformLocation;
    }

    public void setUniform(String name, FloatBuffer value) {
        GL30.glUniformMatrix4fv(getUniformLocation(name), false, value);
    }

    public void setUniform(String name, Matrix4f value) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(4 * 4);
        try {
            value.get(buffer);
            setUniform(name, buffer);
        } finally {
            MemoryUtil.memFree(buffer);
        }
    }

    public void setUniform(String name, int value) {
        GL30.glUniform1i(getUniformLocation(name), value);
    }

    public void bind() {
        GL30.glUseProgram(program);
    }

    public void unbind() {
        GL30.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        GL30.glDeleteProgram(program);
    }
}
