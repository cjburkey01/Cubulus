package com.cjburkey.cubulus.chunk;

import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubulus.block.Block;

public final class ChunkData {
	
	private static final int chunkSize = 16;
	private Block[][][] blocks;
	private final Vector3i pos;
	
	public ChunkData(int chunkX, int chunkY, int chunkZ) {
		blocks = new Block[chunkSize][chunkSize][chunkSize];
		pos = new Vector3i(chunkX, chunkY, chunkZ);
	}
	
	public Vector3f getWorldCoordsf() {
		Vector3f out = new Vector3f(pos.x, pos.y, pos.z);
		return out.mul(chunkSize);
	}
	
	public Vector3i getWorldCoordsi() {
		Vector3i out = new Vector3i(pos);
		return out.mul(chunkSize);
	}
	
	public Vector3i getChunkCoords() {
		return pos;
	}
	
	public void setBlock(int x, int y, int z, Block block) {
		if(inChunk(x, y, z)) {
			blocks[x][y][z] = block;
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		if(inChunk(x, y, z)) {
			return blocks[x][y][z];
		}
		return null;
	}
	
	public boolean inChunk(int x, int y, int z) {
		if(x < 0 || x >= chunkSize) return false;
		if(y < 0 || y >= chunkSize) return false;
		if(z < 0 || z >= chunkSize) return false;
		return true;
	}
	
	public static int getChunkSize() {
		return chunkSize;
	}
	
}