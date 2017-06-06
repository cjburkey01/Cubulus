package com.cjburkey.cubulus.chunk;

import java.util.HashMap;
import java.util.Map.Entry;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.object.GameObject;
import com.cjburkey.cubulus.render.Texture;
import com.cjburkey.cubulus.world.ChunkGeneration;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ChunkHandler {
	
	private final BiMap<ChunkData, GameObject> chunks;
	
	public ChunkHandler() {
		chunks = HashBiMap.create(new HashMap<>());
	}
	
	public void addChunk(int x, int y, int z) {
		ChunkData chunk = new ChunkData(x, y, z);
		ChunkGeneration.generateChunk(chunk);
		spawnMesh(chunk);
	}
	
	public void removeChunk(int x, int y, int z) {
		ChunkData chunk = getChunkAtPos(x, y, z);
		if(chunk != null) {
			GameLogicCore.getInstance().removeGameObject(chunks.get(chunk));
			chunks.remove(chunk);
		}
	}
	
	public void ensureChunksAround(Vector3f worldCoords, int chunkRadius) {
		for(int x = -chunkRadius; x <= chunkRadius; x ++) {
			//for(int y = -chunkRadius; y <= chunkRadius; y ++) {
			int y = 0;
			for(int z = -chunkRadius; z <= chunkRadius; z ++) {
				Vector3i chunkCoords = worldCoordsToChunk(worldCoords);
				chunkCoords.add(x, y, z);
				if(getChunkAtPos(chunkCoords.x, /*chunkCoords.y*/ 0, chunkCoords.z) == null) {
					addChunk(chunkCoords.x, /*chunkCoords.y*/ 0, chunkCoords.z);
				}
			}
			//}
		}
	}
	
	/*public void ensureChunksAround(Vector3f worldCoords, int chunkRadius) {
		Vector3i chunkCoords = worldCoordsToChunk(worldCoords);
		if(getChunkAtPos(chunkCoords.x, chunkCoords.y, chunkCoords.z) == null) {
			addChunk(chunkCoords.x, chunkCoords.y, chunkCoords.z);
		}
	}*/
	
	public ChunkData getChunkAtPos(int x, int y, int z) {
		for(Entry<ChunkData, GameObject> entry : chunks.entrySet()) {
			Vector3i pos = entry.getKey().getChunkCoords();
			if(pos.x == x && pos.y == y && pos.z == z) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public Vector3i worldCoordsToChunk(Vector3f world) {
		int chunkX = (int) Math.floor(world.x / (float) ChunkData.getChunkSize());
		int chunkY = (int) Math.floor(world.y / (float) ChunkData.getChunkSize());
		int chunkZ = (int) Math.floor(world.z / (float) ChunkData.getChunkSize());
		return new Vector3i(chunkX, chunkY, chunkZ);
	}
	
	private void spawnMesh(ChunkData chunk) {
		GameObject obj = new GameObject(null);
		chunks.put(chunk, obj);
		Cubulus.getInstance().runLater(() -> {
			Vector3f pos = chunk.getWorldCoords();
			obj.updateMesh(MeshChunk.generateMeshForChunk(chunk, new Texture("cubulus:texture/block/block_stone.png")));
			obj.setPosition(pos.x, pos.y, pos.z);
			GameLogicCore.getInstance().addGameObject(obj);
		});
	}
	
}