package com.cjburkey.cubulus.world;

import org.joml.Vector3f;
import com.cjburkey.cubulus.chunk.ChunkHandler;

public class World {
	
	public static final int loadRadius = 3;		// In Chunks
	public static final int unloadRadius = 4;	// In Chunks

	private final int chunkChecksPerSecond = 2;
	private final double betweenChunkChecks = 1000000000d / (double) chunkChecksPerSecond;
	private long lastChunkCheck = System.nanoTime();
	
	private ChunkHandler chunkHandler;
	
	public World() {
		chunkHandler = new ChunkHandler();
	}
	
	public void loadChunksAround(Vector3f pos, int chunkRadius) {
		long now = System.nanoTime();
		if(now - lastChunkCheck >= betweenChunkChecks) {
			lastChunkCheck = now;
			for(int x = -chunkRadius; x <= chunkRadius; x ++) {
				for(int y = -chunkRadius; y <= chunkRadius; y ++) {
					for(int z = -chunkRadius; z <= chunkRadius; z ++) {
						generateAndRender(x, y, z);
					}
				}
			}
		}
	}
	
	private void generateAndRender(int x, int y, int z) {
		if(chunkHandler.generateChunk(x, y, z)) {
			chunkHandler.renderChunk(x, y, z);
		}
	}
	
	public ChunkHandler getChunkHandler() {
		return chunkHandler;
	}
	
	public static int getLoadRadius() {
		return loadRadius;
	}
	
}