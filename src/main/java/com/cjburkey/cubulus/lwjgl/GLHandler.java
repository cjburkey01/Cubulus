package com.cjburkey.cubulus.lwjgl;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.shader.ShaderProgram;

public final class GLHandler {
	
	private static ShaderProgram shaderProgram;
	
	public static void clear(Vector3f clearColor) {
		if(Cubulus.onRenderThread()) {
			GL11.glClearColor(clearColor.x, clearColor.y, clearColor.z, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		}
	}
	
	public static ShaderProgram createAndLinkShader() {
		shaderProgram = new ShaderProgram();
		try {
			shaderProgram.createVertexShader(Utils.loadResourceAsString("cubulus:shader/basic/vertex.vs"));
			shaderProgram.createFragmentShader(Utils.loadResourceAsString("cubulus:shader/basic/fragment.fs"));
			shaderProgram.link();
			return shaderProgram;
		} catch(Exception e) {
			Cubulus.err("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
}