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
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_FRONT);
		GL11.glClearColor(0.15f, 0.0f, 0.25f, 1.0f);
		initWireframe(false, false);
		
		try {
			shaderBasic = new ShaderProgram();
			shaderBasic.createVertex(Utils.loadResourceAsString("cubulus:shader/basic/basicVertex.vs"));
			shaderBasic.createFragment(Utils.loadResourceAsString("cubulus:shader/basic/basicFragment.fs"));
			shaderBasic.link();
			setupUniforms(shaderBasic);
		} catch(Exception e) {
			Cubulus.getInstance().error(-182, true, "Could not load shader.");
		}
	}
	
	private void setupUniforms(ShaderProgram program) {
		program.createUniform("projectionMatrix");
		program.createUniform("modelViewMatrix");
		program.createUniform("texture_sampler");
		program.createMaterialUniform("material");
	}
	
	private int prev = 0;
	public void render(GameObject[] gameItems) {
		clear();
		shaderBasic.bind();
		Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Cubulus.getGameWindow().getWidth(), Cubulus.getGameWindow().getHeight(), Z_NEAR, Z_FAR);
		Matrix4f viewMatrix = transform.getViewMatrix(camera);
		shaderBasic.setUniform("projectionMatrix", projectionMatrix);
		shaderBasic.setUniform("texture_sampler", 0);
		if(prev != gameItems.length) {
			prev = gameItems.length;
			Cubulus.info("Rendering " + prev + " objects.");
		}
		for(GameObject item : gameItems) {
			if(item != null && item.getMesh() != null) {
				Matrix4f modelViewMatrix = transform.getModelViewMatrix(item, viewMatrix);
				shaderBasic.setUniform("modelViewMatrix", modelViewMatrix);
				shaderBasic.setUniform("material", item.getMesh().getMaterial());
				item.getMesh().render();
			}
		}
		shaderBasic.unbind();
	}
	
	private void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	private void initWireframe(boolean wireframe, boolean vertex) {
		if(wireframe) {
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			int write = (vertex) ? GL11.GL_POINT : GL11.GL_LINE;
			GL11.glPolygonMode(GL11.GL_FRONT, write);
			GL11.glPolygonMode(GL11.GL_BACK, write);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, write);
		}
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