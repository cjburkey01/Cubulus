package com.cjburkey.cubulus.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.object.Mesh;
import com.cjburkey.cubulus.shader.ShaderProgram;

public final class Renderer {
	
	private ShaderProgram shaderBasic;
	private Mesh testMesh;
	
	public void init() {
		try {
			shaderBasic = new ShaderProgram();
			shaderBasic.createVertex(Utils.loadResource("/shader/basic/basic.vs"));
			shaderBasic.createFragment(Utils.loadResource("/shader/basic/basic.fs"));
			shaderBasic.link();
			
			testMesh = new Mesh(new float[] {
					-0.5f, 0.5f, 0.0f,
					-0.5f, -0.5f, 0.0f,
					0.5f, -0.5f, 0.0f,
					0.5f, 0.5f, 0.0f
			}, new int[] {
					0, 1, 3, 3, 1, 2
			});
		} catch(Exception e) {
			Cubulus.getInstance().error(-182, true, "Could not load shader.");
		}
	}
	
	public void render() {
		render(testMesh);
	}
	
	public void render(Mesh mesh) {
		shaderBasic.bind();
		GL30.glBindVertexArray(mesh.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shaderBasic.unbind();
	}
	
	public void clear() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		Cubulus.info("Cleaning up renderer...");
		if(shaderBasic != null) {
			shaderBasic.cleanup();
		}
		testMesh.cleanUp();
		Cubulus.info("Cleaned up.");
	}
	
}