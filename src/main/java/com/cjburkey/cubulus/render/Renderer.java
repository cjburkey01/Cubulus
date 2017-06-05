package com.cjburkey.cubulus.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.object.GameItem;
import com.cjburkey.cubulus.object.Transformation;
import com.cjburkey.cubulus.shader.ShaderProgram;

public final class Renderer {
	
	private static final float FOV = (float) Math.toRadians(90.0f);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000.f;
	
	private ShaderProgram shaderBasic;
	private Transformation transform;
	
	public void init() {
		transform = new Transformation();
		
		try {
			shaderBasic = new ShaderProgram();
			shaderBasic.createVertex(Utils.loadResource("/shader/basic/basic.vs"));
			shaderBasic.createFragment(Utils.loadResource("/shader/basic/basic.fs"));
			shaderBasic.link();
			shaderBasic.createUniform("projectionMatrix");
			shaderBasic.createUniform("worldMatrix");
			shaderBasic.createUniform("texture_sampler");
		} catch(Exception e) {
			Cubulus.getInstance().error(-182, true, "Could not load shader.");
		}
	}
	
	public void render(GameItem[] gameItems) {
		clear();
		shaderBasic.bind();
		Matrix4f projection = transform.getProjectionMatrix(FOV, Cubulus.getGameWindow().getWidth(), Cubulus.getGameWindow().getHeight(), Z_NEAR, Z_FAR);
		shaderBasic.setUniform("projectionMatrix", projection);
		shaderBasic.setUniform("texture_sampler", 0);
		for(GameItem item : gameItems) {
			Matrix4f world = transform.getWorldMatrix(item.getPosition(), item.getRotation(), item.getScale());
			shaderBasic.setUniform("worldMatrix", world);
			item.getMesh().render();
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
	
}