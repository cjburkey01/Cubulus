package com.cjburkey.cubulus.object;

import com.cjburkey.cubulus.chunk.ChunkData;
import com.cjburkey.cubulus.chunk.MeshChunk;
import com.cjburkey.cubulus.logic.GameLogicCore;

public class GameObjectChunk extends GameObject {
	
	private ChunkData chunk;

	public GameObjectChunk(ChunkData chunk) {
		super(null);
		this.chunk = chunk;
	}
	
	public void render() {
		if(getMesh() != null) getMesh().cleanUp();
		Mesh m = MeshChunk.generateMeshForChunk(chunk, GameLogicCore.getInstance().getAtlas());
		m.build();
		updateMesh(m);
		GameLogicCore.getInstance().addGameObject(this);
	}
	
	public ChunkData getChunk() {
		return chunk;
	}
	
}