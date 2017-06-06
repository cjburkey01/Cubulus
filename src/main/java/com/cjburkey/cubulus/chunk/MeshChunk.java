package com.cjburkey.cubulus.chunk;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.io.TextureAtlas;
import com.cjburkey.cubulus.light.Material;
import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.object.Mesh;

public final class MeshChunk {
	
	private static final int chunkSize = ChunkData.getChunkSize();
	private static final Vector3f right = new Vector3f(1, 0, 0);
	private static final Vector3f up = new Vector3f(0, 1, 0);
	private static final Vector3f forward = new Vector3f(0, 0, 1);
	
	private static int atlasWidth = 1;
	private static float atlasStep;
	
	public static Mesh generateMeshForChunk(ChunkData chunk, TextureAtlas texture) {
		atlasWidth = texture.getWidth();
		atlasStep = 1f / (float) atlasWidth;
		
		List<Vector3f> verts = new ArrayList<>();
		List<Vector3f> norms = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		for(int x = 0; x < chunkSize; x ++) {
			for(int y = 0; y < chunkSize; y ++) {
				for(int z = 0; z < chunkSize; z ++) {
					Block block = chunk.getBlock(x, y, z);
					if(block != null){
						addBlock(chunk, block, x, y, z, verts, uvs, indices, norms);
					}
				}
			}
		}
		
		float[] vertsArr = floatFromVec3List(verts);
		float[] normsArr = floatFromVec3List(norms);
		float[] uvsArr = floatFromVec2List(uvs);
		int[] indicesArr = intFromIntList(indices);
		Mesh mesh = new Mesh(vertsArr, normsArr, uvsArr, indicesArr);
		mesh.setMaterial(new Material(texture.getTexture()));
		return mesh;
	}
	
	private static void addBlock(ChunkData chunk, Block block, int x, int y, int z, List<Vector3f> verts, List<Vector2f> uvs, List<Integer> inds, List<Vector3f> norms) {
		if(transparentBlockAt(chunk, x - 1, y, z)) {
			addBlockFace(block, new Vector3f(x, y, z), up, forward, new Vector3f(-1, 0, 0), false, verts, inds, uvs, norms);				// Left
		}
		
		if(transparentBlockAt(chunk, x + 1, y, z)) {
			addBlockFace(block, new Vector3f(x + 1.0f, y, z), up, forward, new Vector3f(1, 0, 0), true, verts, inds, uvs, norms);		// Right
		}
		
		if(transparentBlockAt(chunk, x, y - 1, z)) {
			addBlockFace(block, new Vector3f(x, y, z), forward, right, new Vector3f(0, -1, 0), false, verts, inds, uvs, norms);			// Bottom
		}

		if(transparentBlockAt(chunk, x, y + 1, z)) {
			addBlockFace(block, new Vector3f(x, y + 1.0f, z), forward, right, new Vector3f(0, 1, 0), true, verts, inds, uvs, norms);	// Top
		}
		
		if(transparentBlockAt(chunk, x, y, z - 1)) {
			addBlockFace(block, new Vector3f(x, y, z), up, right, new Vector3f(0, 0, -1), true, verts, inds, uvs, norms);				// Front
		}

		if(transparentBlockAt(chunk, x, y, z + 1)) {
			addBlockFace(block, new Vector3f(x, y, z + 1.0f), up, right, new Vector3f(0, 0, 1), false, verts, inds, uvs, norms);		// Front
		}
	}
	
	private static void addBlockFace(Block block, Vector3f corner, Vector3f up, Vector3f right, Vector3f normal, boolean rev, List<Vector3f> verts, List<Integer> tris, List<Vector2f> uvs, List<Vector3f> norms) {
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
		
		final Vector2i forBlock = GameLogicCore.getInstance().getAtlas().forBlock(block);
		
		uvs.add(new Vector2f(atlasStep * forBlock.x, atlasStep * forBlock.y));
		uvs.add(new Vector2f(atlasStep * forBlock.x, atlasStep + atlasStep * forBlock.y));
		uvs.add(new Vector2f(atlasStep + atlasStep * forBlock.x, atlasStep + atlasStep * forBlock.y));
		uvs.add(new Vector2f(atlasStep + atlasStep * forBlock.x, atlasStep * forBlock.y));
		
		norms.add(normal);
	}
	
	private static boolean transparentBlockAt(ChunkData data, int x, int y, int z) {
		return GameLogicCore.getInstance().getWorld().getChunkHandler().transparentBlockAt(data, x, y, z);
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
	
}