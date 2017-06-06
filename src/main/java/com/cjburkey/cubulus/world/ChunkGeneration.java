package com.cjburkey.cubulus.world;

import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.block.Blocks;
import com.cjburkey.cubulus.chunk.ChunkData;

public final class ChunkGeneration {
	
	public static void generateChunk(ChunkData chunk) {
		Cubulus.info("Generating chunk: " + chunk.getChunkCoords() + "...");
		int size = ChunkData.getChunkSize();
		for(int x = 0; x < size; x ++) {
			for(int y = 0; y < size; y ++) {
				for(int z = 0; z < size; z ++) {
					if(Utils.randomRangei(0, 16, true) > y) {
						chunk.setBlock(x, y, z, Blocks.blockStone);
					}
				}
			}
		}
		Cubulus.info("  ...Done");
	}
	
}