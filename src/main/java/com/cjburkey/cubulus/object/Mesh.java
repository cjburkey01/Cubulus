package com.cjburkey.cubulus.object;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.cubulus.render.Texture;

public class Mesh {
	
	private final int vao;
	private final int vbo;
	private final int idxVbo;
	private final int uvVbo;
	private final int vertCount;
	private final Vector3f color;
	private final Texture texture;
	
	public Mesh(float[] verts, float[] normals, float[] uvs, int[] inds, Texture texture) {
		this(verts, normals, uvs, inds, texture, new Vector3f());
	}
	
	public Mesh(float[] verts, float[] normals, float[] uvs, int[] inds, Vector3f color) {
		this(verts, normals, uvs, inds, null, color);
	}
	
	private Mesh(float[] verts, float[] normals, float[] uvs, int[] inds, Texture texture, Vector3f color) {
		this.texture = texture;
		this.color = color;
		
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
		
		uvVbo = GL15.glGenBuffers();
		FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(uvs.length);
		colorBuffer.put(uvs).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, uvVbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		idxVbo = GL15.glGenBuffers();
		IntBuffer indBuffer = MemoryUtil.memAllocInt(inds.length);
		indBuffer.put(inds).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, idxVbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		
		MemoryUtil.memFree(vertBuffer);
		MemoryUtil.memFree(indBuffer);
		MemoryUtil.memFree(colorBuffer);
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public boolean useColor() {
		return texture == null;
	}
	
	public Vector3f getColor() {
		return color;
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
	
	public void render() {
		if(texture != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		}
		GL30.glBindVertexArray(getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawElements(GL11.GL_TRIANGLES, getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	public void cleanUp() {
		GL20.glDisableVertexAttribArray(0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(idxVbo);
		GL15.glDeleteBuffers(uvVbo);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vao);
	}
	
}