#version 330

in vec2 outTextureCoord;
out vec4 fragColor;

uniform sampler2D textureUnit;

void main() {
    fragColor = texture(textureUnit, outTextureCoord);
}
