package com.cjburkey.cubulus.world;

import com.cjburkey.cubulus.chunk.ChunkHandler;

public class World {
	
	private ChunkHandler chunkHandler;
	
	public World() {
		chunkHandler = new ChunkHandler();
	}
	
	public ChunkHandler getChunkHandler() {
		return chunkHandler;
	}
	
}