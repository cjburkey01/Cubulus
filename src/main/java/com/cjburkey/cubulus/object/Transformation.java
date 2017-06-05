package com.cjburkey.cubulus.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.cjburkey.cubulus.render.Camera;

public final class Transformation {
	
	private final Matrix4f projectionMatrix;
	private final Matrix4f modelViewMatrix;
	private final Matrix4f viewMatrix;
	
	public Transformation() {
		projectionMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
	}
	
	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspect = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspect, zNear, zFar);
		return projectionMatrix;
	}
	
	public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
		Vector3f rot = gameItem.getRotation();
		
		modelViewMatrix.identity();
		modelViewMatrix.translate(gameItem.getPosition());
		modelViewMatrix.rotateX((float) Math.toRadians(-rot.x));
		modelViewMatrix.rotateY((float) Math.toRadians(-rot.y));
		modelViewMatrix.rotateZ((float) Math.toRadians(-rot.z));
		modelViewMatrix.scale(gameItem.getScale());
		
		return viewMatrix.mul(modelViewMatrix);
	}
	
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f camPos = camera.getPosition();
		Vector3f camRot = camera.getRotation();
		
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(camRot.x), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(camRot.y), new Vector3f(0, 1, 0));
		viewMatrix.rotate((float) Math.toRadians(camRot.z), new Vector3f(0, 0, 1));
		viewMatrix.translate(-camPos.x, -camPos.y, -camPos.z);
		
		return viewMatrix;
	}
	
}