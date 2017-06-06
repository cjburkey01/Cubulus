package com.cjburkey.cubulus.object;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.cubulus.light.Material;
import com.cjburkey.cubulus.render.Texture;

public class Mesh {
	
	private int vaoId;
	private List<Integer> vboIdList;
	private int vertexCount;
	private Material material;
	
	private final float[] verts;
	private final float[] norms;
	private final float[] uvs;
	private final int[] tris;
	
	public Mesh(float[] verts, float[] normals, float[] uvs, int[] inds) {
		this.verts = verts;
		this.norms = normals;
		this.uvs = uvs;
		tris = inds;
	}
	
	public void build() {
		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		FloatBuffer vecNormalsBuffer = null;
		IntBuffer indicesBuffer = null;
		try {
			vertexCount = tris.length;
			vboIdList = new ArrayList<>();
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			
			int vboId = GL15.glGenBuffers();
			vboIdList.add(vboId);
			posBuffer = MemoryUtil.memAllocFloat(verts.length);
			posBuffer.put(verts).flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, posBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
			
			vboId = GL15.glGenBuffers();
			vboIdList.add(vboId);
			textCoordsBuffer = MemoryUtil.memAllocFloat(uvs.length);
			textCoordsBuffer.put(uvs).flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textCoordsBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
			
			vboId = GL15.glGenBuffers();
			vboIdList.add(vboId);
			vecNormalsBuffer = MemoryUtil.memAllocFloat(norms.length);
			vecNormalsBuffer.put(norms).flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vecNormalsBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
			
			vboId = GL15.glGenBuffers();
			vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(tris.length);
			indicesBuffer.put(tris).flip();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
		} finally {
			if(posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if(textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
			}
			if(vecNormalsBuffer != null) {
				MemoryUtil.memFree(vecNormalsBuffer);
			}
			if(indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void render() {
		Texture texture = material.getTexture();
		if(texture != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
		}
		
		GL30.glBindVertexArray(getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		//GL20.glEnableVertexAttribArray(2);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		//GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void cleanUp() {
		GL20.glDisableVertexAttribArray(0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		for(int vboId : vboIdList) {
			GL15.glDeleteBuffers(vboId);
		}
		
		Texture texture = material.getTexture();
		if(texture != null) {
			texture.cleanup();
		}
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
	}
	
}