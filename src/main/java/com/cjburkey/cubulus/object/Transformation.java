package com.cjburkey.cubulus.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.cjburkey.cubulus.render.Camera;

public final class Transformation {
	
	private final Matrix4f retProjectionMatrix;
	private final Matrix4f retModelViewMatrix;
	private final Matrix4f retViewMatrix;
	
	public Transformation() {
		retProjectionMatrix = new Matrix4f();
		retModelViewMatrix = new Matrix4f();
		retViewMatrix = new Matrix4f();
	}
	
	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspect = width / height;
		retProjectionMatrix.identity();
		retProjectionMatrix.perspective(fov, aspect, zNear, zFar);
		return retProjectionMatrix;
	}
	
	public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
		Vector3f rot = gameItem.getRotation();
		
		retModelViewMatrix.identity();
		retModelViewMatrix.translate(gameItem.getPosition());
		retModelViewMatrix.rotateX((float) Math.toRadians(-rot.x));
		retModelViewMatrix.rotateY((float) Math.toRadians(-rot.y));
		retModelViewMatrix.rotateZ((float) Math.toRadians(-rot.z));
		retModelViewMatrix.scale(gameItem.getScale());
		
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(retModelViewMatrix);
	}
	
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f camPos = camera.getPosition();
		Vector3f camRot = camera.getRotation();
		
		retViewMatrix.identity();
		retViewMatrix.rotate((float) Math.toRadians(camRot.x), new Vector3f(1, 0, 0));
		retViewMatrix.rotate((float) Math.toRadians(camRot.y), new Vector3f(0, 1, 0));
		retViewMatrix.rotate((float) Math.toRadians(camRot.z), new Vector3f(0, 0, 1));
		retViewMatrix.translate(-camPos.x, -camPos.y, -camPos.z);
		
		return retViewMatrix;
	}
	
}