package com.cjburkey.cubulus.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.object.GameObject;
import com.cjburkey.cubulus.object.Transformation;
import com.cjburkey.cubulus.shader.ShaderProgram;

public final class Renderer {
	
	private static final float FOV = (float) Math.toRadians(90.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
	
	private ShaderProgram shaderBasic;
	private Transformation transform;
	private Camera camera;
	
	public void init() {
		transform = new Transformation();
		camera = new Camera();
		
		try {
			shaderBasic = new ShaderProgram();
			shaderBasic.createVertex(Utils.loadResourceAsString("cubulus:shader/basic/basicVertex.vs"));
			shaderBasic.createFragment(Utils.loadResourceAsString("cubulus:shader/basic/basicFragment.fs"));
			shaderBasic.link();
			shaderBasic.createUniform("projectionMatrix");
			shaderBasic.createUniform("modelViewMatrix");
			shaderBasic.createUniform("texture_sampler");
			shaderBasic.createUniform("color");
			shaderBasic.createUniform("useColor");
		} catch(Exception e) {
			Cubulus.getInstance().error(-182, true, "Could not load shader.");
		}
	}
	
	public void render(GameObject[] gameItems) {
		clear();
		shaderBasic.bind();
		Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Cubulus.getGameWindow().getWidth(), Cubulus.getGameWindow().getHeight(), Z_NEAR, Z_FAR);
		Matrix4f viewMatrix = transform.getViewMatrix(camera);
		shaderBasic.setUniform("projectionMatrix", projectionMatrix);
		shaderBasic.setUniform("texture_sampler", 0);
		for(GameObject item : gameItems) {
			if(item != null && item.getMesh() != null) {
				Matrix4f modelViewMatrix = transform.getModelViewMatrix(item, viewMatrix);
				shaderBasic.setUniform("modelViewMatrix", modelViewMatrix);
				shaderBasic.setUniform("color", item.getMesh().getColor());
				shaderBasic.setUniform("useColor", (item.getMesh().useColor()) ? 1 : 0);
				item.getMesh().render();
			}
		}
		shaderBasic.unbind();
	}
	
	private void clear() {
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		Cubulus.info("Cleaning up renderer...");
		if(shaderBasic != null) {
			shaderBasic.cleanup();
		}
		Cubulus.info("Cleaned up.");
	}
	
	public Camera getCamera() {
		return camera;
	}
	
}