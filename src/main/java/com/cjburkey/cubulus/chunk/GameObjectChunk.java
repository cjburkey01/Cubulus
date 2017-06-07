package com.cjburkey.cubulus.chunk;

import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.object.GameObject;
import com.cjburkey.cubulus.object.Mesh;

public final class GameObjectChunk extends GameObject {
	
	private ChunkData chunk;

	private GameObjectChunk(Mesh mesh) {
		super(mesh);
	}
	
	public ChunkData getChunk() {
		return chunk;
	}
	
	public static GameObjectChunk createGameObject(ChunkData chunk) {
		Mesh mesh = generateNewMesh(chunk);
		GameObjectChunk generated = new GameObjectChunk(mesh);
		generated.chunk = chunk;
		generated.setPosition(chunk.getWorldCoordsf().x, chunk.getWorldCoordsf().y, chunk.getWorldCoordsf().z);
		GameLogicCore.getInstance().addGameObject(generated);
		return generated;
	}
	
	private static Mesh generateNewMesh(ChunkData chunk) {
		Mesh mesh = MeshChunk.generateMeshForChunk(chunk, GameLogicCore.getInstance().getAtlas());
		mesh.build();
		return mesh;
	}
	
}