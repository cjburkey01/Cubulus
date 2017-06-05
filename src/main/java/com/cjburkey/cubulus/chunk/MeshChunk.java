package com.cjburkey.cubulus.chunk;

import org.joml.Vector3f;
import com.cjburkey.cubulus.object.Mesh;

public final class MeshChunk extends Mesh {
	
	private static final int chunkSize = 16;
	
	private static final float[] verts = new float[] {
			(float) -chunkSize / 2f, (float) chunkSize / 2f, (float) chunkSize / 2f,
			(float) -chunkSize / 2f, (float) -chunkSize / 2f, (float) chunkSize / 2f,
			(float) chunkSize / 2f, (float) -chunkSize / 2f, (float) chunkSize / 2f,
			(float) chunkSize / 2f, (float) chunkSize / 2f, (float) chunkSize / 2f,
			(float) -chunkSize / 2f, (float) chunkSize / 2f, (float) -chunkSize / 2f,
			(float) chunkSize / 2f, (float) chunkSize / 2f, (float) -chunkSize / 2f,
			(float) -chunkSize / 2f, (float) -chunkSize / 2f, (float) -chunkSize / 2f,
			(float) chunkSize / 2f, (float) -chunkSize / 2f, (float) -chunkSize / 2f,
			(float) -chunkSize / 2f, (float) chunkSize / 2f, (float) -chunkSize / 2f,
			(float) chunkSize / 2f, (float) chunkSize / 2f, (float) -chunkSize / 2f,
			(float) -chunkSize / 2f, (float) chunkSize / 2f, (float) chunkSize / 2f,
			(float) chunkSize / 2f, (float) chunkSize / 2f, (float) chunkSize / 2f,
			(float) chunkSize / 2f, (float) chunkSize / 2f, (float) chunkSize / 2f,
			(float) chunkSize / 2f, (float) -chunkSize / 2f, (float) chunkSize / 2f,
			(float) -chunkSize / 2f, (float) chunkSize / 2f, (float) chunkSize / 2f,
			(float) -chunkSize / 2f, (float) -chunkSize / 2f, (float) chunkSize / 2f,
			(float) -chunkSize / 2f, (float) -chunkSize / 2f, (float) -chunkSize / 2f,
			(float) chunkSize / 2f, (float) -chunkSize / 2f, (float) -chunkSize / 2f,
			(float) -chunkSize / 2f, (float) -chunkSize / 2f, (float) chunkSize / 2f,
			(float) chunkSize / 2f, (float) -chunkSize / 2f, (float) chunkSize / 2f
	};
	
	private static final float[] normals = new float[] {
			
	};

	private static final float[] uvs = new float[] {
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 1.0f,
			0.5f, 1.0f,
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.0f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			1.0f, 0.0f,
			0.5f, 0.5f,
			1.0f, 0.5f
	};
	
	private static final int[] inds = new int[] {
			0, 1, 3, 3, 1, 2,
			8, 10, 11, 9, 8, 11,
			12, 13, 7, 5, 12, 7,
			14, 15, 6, 4, 14, 6,
			16, 18, 19, 17, 16, 19,
			4, 6, 7, 5, 4, 7
	};
	
	public static MeshChunk generateMeshForChunk() {
		return null;
	}
	
	public MeshChunk(String texture) {
		super(verts, normals, uvs, inds, texture);
	}
	
	public MeshChunk(Vector3f color) {
		super(verts, normals, uvs, inds, color);
	}
	
}