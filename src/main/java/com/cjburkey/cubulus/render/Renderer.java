package com.cjburkey.cubulus.render;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.shader.ShaderProgram;

public final class Renderer {
	
	private ShaderProgram shaderBasic;
	private int vao;
	private int vbo;
	
	public void init() {
		try {
			shaderBasic = new ShaderProgram();
			shaderBasic.createVertex(Utils.loadResource("/shader/basic/basic.vs"));
			shaderBasic.createFragment(Utils.loadResource("/shader/basic/basic.fs"));
			
			float[] verts = new float[] {
					0.0f, 0.5f, 0.0f,
					-0.5f, -0.5f, 0.0f,
					0.5f, -0.5f, 0.0f,
			};
			FloatBuffer vertBuffer = MemoryUtil.memAllocFloat(verts.length);
			vertBuffer.put(verts).flip();
			
			vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			
			vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuffer, GL15.GL_STATIC_DRAW);
			MemoryUtil.memFree(vertBuffer);
			
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);

			if(vertBuffer != null) {
				MemoryUtil.memFree(vertBuffer);
			}
			
			shaderBasic.link();
		} catch(Exception e) {
			Cubulus.getInstance().error(-182, true, "Could not load shader.");
		}
	}
	
	public void render() {
		shaderBasic.bind();
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shaderBasic.unbind();
	}
	
	public void clear() {
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}