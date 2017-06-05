package com.cjburkey.cubulus.object;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	
	private final int vao;
	private final int vbo;
	private final int vertCount;
	
	public Mesh(float[] verts) {
		FloatBuffer vertBuffer = MemoryUtil.memAllocFloat(verts.length);
		vertCount = verts.length / 3;
		vertBuffer.put(verts).flip();
		
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		
		if(vertBuffer != null) {
			MemoryUtil.memFree(vertBuffer);
		}
	}
	
	public int getVaoId() {
		return vao;
	}
	
	public int getVboId() {
		return vbo;
	}
	
	public void cleanUp() {
		GL20.glDisableVertexAttribArray(0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
	}
	
}