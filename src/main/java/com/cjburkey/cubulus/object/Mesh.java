package com.cjburkey.cubulus.object;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	
	private final int vao;
	private final int vbo;
	private final int idxVbo;
	private final int colorVbo;
	private final int vertCount;
	
	public Mesh(float[] verts, float[] color, int[] inds) {
		FloatBuffer vertBuffer = MemoryUtil.memAllocFloat(verts.length);
		vertCount = inds.length;
		vertBuffer.put(verts).flip();
		
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		idxVbo = GL15.glGenBuffers();
		IntBuffer indBuffer = MemoryUtil.memAllocInt(inds.length);
		indBuffer.put(inds).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxVbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		colorVbo = GL15.glGenBuffers();
		FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(color.length);
		colorBuffer.put(color).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		
		MemoryUtil.memFree(vertBuffer);
		MemoryUtil.memFree(indBuffer);
		MemoryUtil.memFree(colorBuffer);
	}
	
	public int getVaoId() {
		return vao;
	}
	
	public int getVboId() {
		return vbo;
	}
	
	public int getVertexCount() {
		return vertCount;
	}
	
	public void cleanUp() {
		GL20.glDisableVertexAttribArray(0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(idxVbo);
		GL15.glDeleteBuffers(colorVbo);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
	}
	
}