#version 330

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

struct Material {
	vec3 color;
	int hasTexture;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform Material material;

void main() {
	if(material.hasTexture == 1) {
		fragColor = texture(texture_sampler, outTexCoord);
	} else {
		fragColor = vec4(material.color, 1.0);
	}
}