package com.cjburkey.cubulus.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Transformation {
	
	private final Matrix4f projectionMatrix;
	private final Matrix4f worldMatrix;
	
	public Transformation() {
		projectionMatrix = new Matrix4f();
		worldMatrix = new Matrix4f();
	}
	
	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspect = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspect, zNear, zFar);
		return projectionMatrix;
	}
	
	public Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		worldMatrix.identity();
		worldMatrix.translate(offset);
		worldMatrix.rotateX((float) Math.toRadians(rotation.x));
		worldMatrix.rotateY((float) Math.toRadians(rotation.y));
		worldMatrix.rotateZ((float) Math.toRadians(rotation.z));
		worldMatrix.scale(scale);
		return worldMatrix;
	}
	
}