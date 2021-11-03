#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 inTextureCoord;

out vec2 outTextureCoord;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main() {
    gl_Position = projectionMatrix * transformationMatrix * vec4(position, 1.0);
    outTextureCoord = inTextureCoord;
}
