#version 330

in vec2 outTextureCoord;
out vec4 fragColor;

uniform sampler2D textureUnit;

void main() {
    vec4 color = texture(textureUnit, outTextureCoord);
    //color*=color; // Make it more vibrant
    fragColor = color;
}
