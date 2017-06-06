package com.cjburkey.cubulus.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.light.DirectionalLight;
import com.cjburkey.cubulus.light.PointLight;
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
		program.createUniform("specularPower");
		program.createUniform("ambientLight");
		program.createPointLightUniform("pointLight");
		program.createDirectionalLightUniform("directionalLight");
	}
	
	public void render(GameObject[] gameItems, Vector3f ambientLight, PointLight pointLight, DirectionalLight directionalLight) {
		clear();
		shaderBasic.bind();
		Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, Cubulus.getGameWindow().getWidth(), Cubulus.getGameWindow().getHeight(), Z_NEAR, Z_FAR);
		Matrix4f viewMatrix = transform.getViewMatrix(camera);
		shaderBasic.setUniform("projectionMatrix", projectionMatrix);
		shaderBasic.setUniform("ambientLight", ambientLight);
		shaderBasic.setUniform("specularPower", 10.0f);
		
		PointLight currPointLight = new PointLight(pointLight);
		Vector3f lightPos = currPointLight.getPosition();
		Vector4f aux = new Vector4f(lightPos, 1.0f);
		aux.mul(viewMatrix);
		lightPos.x = aux.x;
		lightPos.y = aux.y;
		lightPos.z = aux.z;
		shaderBasic.setUniform("pointLight", currPointLight);
		
		DirectionalLight currDirLight = new DirectionalLight(directionalLight);
		Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
		dir.mul(viewMatrix);
		currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		shaderBasic.setUniform("directionalLight", currDirLight);
		
		shaderBasic.setUniform("texture_sampler", 0);
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
		GL11.glClearColor(0.15f, 0.0f, 0.25f, 1.0f);
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