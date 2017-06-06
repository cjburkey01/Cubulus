package com.cjburkey.cubulus.object;

import org.joml.Vector3f;

public class GameObject {
	
	private Mesh mesh = null;
	private final Vector3f position;
	private float scale;
	private final Vector3f rotation;
	
	public GameObject(Mesh mesh) {
		updateMesh(mesh);
		position = new Vector3f(0.0f, 0.0f, 0.0f);
		scale = 1;
		rotation = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	public void updateMesh(Mesh mesh) {
		if(mesh != null) {
			this.mesh = mesh;
		}
	}
	
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getScale() {
		return scale;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
}