package com.cjburkey.cubulus.render;

import org.joml.Vector3f;

public class Material {
	
	private final Vector3f color;
	private final Texture texture;
	
	public Material(Vector3f color, Texture texture) {
		if(texture != null) {
			this.color = new Vector3f(1.0f, 1.0f, 1.0f);
			this.texture = texture;
		} else {
			this.color = color;
			this.texture = null;
		}
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public boolean hasTexture() {
		return texture != null;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
}