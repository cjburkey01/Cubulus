package com.cjburkey.cubulus.world;

import org.joml.Vector3f;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.block.Blocks;
import com.cjburkey.cubulus.chunk.ChunkData;

public final class ChunkGeneration {
	
	private static final double scale = 0.025d;
	
	public static void generateChunk(ChunkData chunk) {
		Cubulus.info("Generating chunk: " + chunk.getChunkCoords() + "...");
		int size = ChunkData.getChunkSize();
		for(int x = 0; x < size; x ++) {
			for(int y = 0; y < size; y ++) {
				for(int z = 0; z < size; z ++) {
					Vector3f chunkPos = chunk.getWorldCoordsf();
					Block block = getBlockAt(chunkPos.add(new Vector3f(x, y, z)));
					chunk.setBlock(x, y, z, block);
				}
			}
		}
		Cubulus.info("  ...Done");
	}
	
	private static Block getBlockAt(Vector3f pos) {
		pos.mul((float) scale);
		double noise = SimplexNoise.noise(pos.x, pos.y, pos.z);
		return (noise >= 0.3d) ? randomBlock() : null;
	}
	
	private static Block randomBlock() {
		Block[] blocks = Blocks.getAllBlocks();
		return blocks[Utils.randomRangei(0, blocks.length, false)];
	}
	
}