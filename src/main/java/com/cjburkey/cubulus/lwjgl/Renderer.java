package com.cjburkey.cubulus.lwjgl;

import java.nio.FloatBuffer;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.shader.ShaderProgram;

public class Renderer {
	
	private ShaderProgram shader;
	private int vao;
	private int vbo;
	
	public void init() throws Exception {
		shader = new ShaderProgram();
		shader.createVertexShader(Utils.loadResourceAsString("cubulus:shader/basic/vertex.vs"));
		shader.createFragmentShader(Utils.loadResourceAsString("cubulus:shader/basic/fragment.fs"));
		shader.link();
		
		final float[] verts = {
				0.0f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
		};
		
		FloatBuffer vertsBuffer = null;
		try {
			vertsBuffer = MemoryUtil.memAllocFloat(verts.length);
			vertsBuffer.put(verts).flip();
			
			vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			
			vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertsBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
		} finally {
			if(vertsBuffer != null) {
				MemoryUtil.memFree(vertsBuffer);
			}
		}
	}
	
	public void render() {
		GLHandler.clear(new Vector3f(1.0f, 1.0f, 1.0f));
		shader.bind();
		
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		ShaderProgram.unbind();
	}
	
	public void cleanup() {
		shader.cleanup();
		GL20.glDisableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
	}
	
}