package com.cjburkey.cubulus.chunk;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.io.TextureAtlas;
import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.object.Mesh;
import com.cjburkey.cubulus.render.Texture;

public final class MeshChunk {
	
	private static int atlasWidth = 1;
	private static final float atlasStep = 1f / (float) atlasWidth;
	private static final int chunkSize = ChunkData.getChunkSize();
	private static final float blockSize = 1.0f;
	private static final Vector3f right = new Vector3f(1, 0, 0);
	private static final Vector3f up = new Vector3f(0, 1, 0);
	private static final Vector3f forward = new Vector3f(0, 0, 1);
	
	public static Mesh generateMeshForChunk(ChunkData chunk, Texture texture) {
		atlasWidth = GameLogicCore.getInstance().getTextureAtlas().getSize();
		List<Vector3f> verts = new ArrayList<>();
		List<Vector3f> norms = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		for(int x = 0; x < chunkSize; x ++) {
			for(int y = 0; y < chunkSize; y ++) {
				for(int z = 0; z < chunkSize; z ++) {
					Block block = chunk.getBlock(x, y, z);
					if(block != null){
						addBlock(chunk, block, x, y, z, verts, uvs, indices);
					}
				}
			}
		}
		
		float[] vertsArr = floatFromVec3List(verts);
		float[] normsArr = floatFromVec3List(norms);
		float[] uvsArr = floatFromVec2List(uvs);
		int[] indicesArr = intFromIntList(indices);
		return new Mesh(vertsArr, normsArr, uvsArr, indicesArr, texture);
	}
	
	private static void addBlock(ChunkData chunk, Block block, int x, int y, int z, List<Vector3f> verts, List<Vector2f> uvs, List<Integer> inds) {
		if(transparentBlockAt(chunk, x - 1, y, z)) {
			addBlockFace(block, new Vector3f(x, y, z), up, forward, false, verts, inds, uvs);				// Left
		}
		
		if(transparentBlockAt(chunk, x + 1, y, z)) {
			addBlockFace(block, new Vector3f(x + blockSize, y, z), up, forward, true, verts, inds, uvs);	// Right
		}
		
		if(transparentBlockAt(chunk, x, y - 1, z)) {
			addBlockFace(block, new Vector3f(x, y, z), forward, right, false, verts, inds, uvs);			// Bottom
		}

		if(transparentBlockAt(chunk, x, y + 1, z)) {
			addBlockFace(block, new Vector3f(x, y + blockSize, z), forward, right, true, verts, inds, uvs);	// Top
		}
		
		if(transparentBlockAt(chunk, x, y, z - 1)) {
			addBlockFace(block, new Vector3f(x, y, z), up, right, true, verts, inds, uvs);					// Front
		}

		if(transparentBlockAt(chunk, x, y, z + 1)) {
			addBlockFace(block, new Vector3f(x, y, z + blockSize), up, right, false, verts, inds, uvs);		// Front
		}
	}
	
	private static boolean transparentBlockAt(ChunkData data, int x, int y, int z) {
		return GameLogicCore.getInstance().getWorld().transparentBlockAt(data, x, y, z);
	}
	
	private static void addBlockFace(Block block, Vector3f corner, Vector3f up, Vector3f right, boolean rev, List<Vector3f> verts, List<Integer> tris, List<Vector2f> uvs) {
		int index = verts.size();
		
		Vector3f i0 = new Vector3f();
		Vector3f i1 = new Vector3f();
		Vector3f i2 = new Vector3f();
		Vector3f i3 = new Vector3f();
		
		i0.add(corner);
		i1.add(corner).add(up);
		i2.add(corner).add(up).add(right);
		i3.add(corner).add(right);
		
		verts.add(i0);
		verts.add(i1);
		verts.add(i2);
		verts.add(i3);
		
		if(rev) {
			tris.add(index + 0);
			tris.add(index + 1);
			tris.add(index + 2);
			tris.add(index + 2);
			tris.add(index + 3);
			tris.add(index + 0);
		} else {
			tris.add(index + 1);
			tris.add(index + 0);
			tris.add(index + 2);
			tris.add(index + 3);
			tris.add(index + 2);
			tris.add(index + 0);
		}
		
		final TextureAtlas ta = GameLogicCore.getInstance().getTextureAtlas();
		uvs.add(new Vector2f(atlasStep * ta.getForBlock(block).x, atlasStep * ta.getForBlock(block).y));
		uvs.add(new Vector2f(atlasStep * ta.getForBlock(block).x, atlasStep + atlasStep * ta.getForBlock(block).y));
		uvs.add(new Vector2f(atlasStep + atlasStep * ta.getForBlock(block).x, atlasStep + atlasStep * ta.getForBlock(block).y));
		uvs.add(new Vector2f(atlasStep + atlasStep * ta.getForBlock(block).x, atlasStep * ta.getForBlock(block).y));
	}
	
	private static float[] floatFromVec3List(List<Vector3f> list) {
		float[] out = new float[list.size() * 3];
		for(int i = 0; i < list.size(); i ++) {
			Vector3f vec = list.get(i);
			out[3 * i] = vec.x;
			out[3 * i + 1] = vec.y;
			out[3 * i + 2] = vec.z;
		}
		return out;
	}
	
	private static float[] floatFromVec2List(List<Vector2f> list) {
		float[] out = new float[list.size() * 2];
		for(int i = 0; i < list.size(); i ++) {
			Vector2f vec = list.get(i);
			out[2 * i] = vec.x;
			out[2 * i + 1] = vec.y;
		}
		return out;
	}
	
	private static int[] intFromIntList(List<Integer> list) {
		int[] out = new int[list.size()];
		for(int i = 0; i < list.size(); i ++) {
			out[i] = list.get(i);
		}
		return out;
	}
	
	// Old code for cube, don't use it anymore, it's really bad.
	// I kept it here because I don't trust myself and might break the new code.
	/*private static final float[] verts = new float[] {
			0.0f, -chunkSize, chunkSize,
			0.0f, 0.0f, chunkSize,
			chunkSize, 0.0f, chunkSize,
			chunkSize, -chunkSize, chunkSize,
			0.0f, -chunkSize, 0.0f,
			chunkSize, -chunkSize, 0.0f,
			0.0f, 0.0f, 0.0f,
			chunkSize, 0.0f, 0.0f,
			0.0f, -chunkSize, 0.0f,
			chunkSize, -chunkSize, 0.0f,
			0.0f, -chunkSize, chunkSize,
			chunkSize, -chunkSize, chunkSize,
			chunkSize, -chunkSize, chunkSize,
			chunkSize, 0.0f, chunkSize,
			0.0f, -chunkSize, chunkSize,
			0.0f, 0.0f, chunkSize,
			0.0f, 0.0f, 0.0f,
			chunkSize, 0.0f, 0.0f,
			0.0f, 0.0f, chunkSize,
			chunkSize, 0.0f, chunkSize
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
	
	private static final float[] normals = new float[] {  };*/
	
}