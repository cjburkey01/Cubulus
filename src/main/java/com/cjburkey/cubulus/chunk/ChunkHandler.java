package com.cjburkey.cubulus.chunk;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.object.GameObject;
import com.cjburkey.cubulus.render.Texture;
import com.cjburkey.cubulus.world.ChunkGeneration;

public class ChunkHandler {
	
	private final Map<ChunkData, GameObject> renderedChunks;
	
	public ChunkHandler() {
		renderedChunks = new ConcurrentHashMap<>();
	}
	
	public void addChunk(int x, int y, int z) {
		new Thread(() -> {
			ChunkData chunk = new ChunkData(x, y, z);
			ChunkGeneration.generateChunk(chunk);
			spawnMesh(chunk);
		}).start();
	}
	
	public void removeChunk(int x, int y, int z) {
		ChunkData chunk = getChunkAtPos(x, y, z);
		if(chunk != null) {
			GameLogicCore.getInstance().removeGameObject(renderedChunks.get(chunk));
			renderedChunks.remove(chunk);
		}
	}
	
	public void ensureChunksAround(Vector3f worldCoords, int chunkRadius) {
		for(int x = -chunkRadius; x <= chunkRadius; x ++) {
			for(int y = -chunkRadius; y <= chunkRadius; y ++) {
				for(int z = -chunkRadius; z <= chunkRadius; z ++) {
					Vector3i chunkCoords = worldCoordsToChunk(worldCoords);
					chunkCoords.add(x, y, z);
					if(getChunkAtPos(chunkCoords.x, chunkCoords.y, chunkCoords.z) == null) {
						addChunk(chunkCoords.x, chunkCoords.y, chunkCoords.z);
					}
				}
			}
		}
	}
	
	public boolean transparentBlockAt(ChunkData data, int x, int y, int z) {
		Block at = data.getBlock(x, y, z);
		final int size = ChunkData.getChunkSize();
		if(at != null) {
			return at.isTransparent();
		} else if(x < 0 || x >= size || y < 0 || y >= size || z < 0 || z >= size) {
			Vector3i world = data.getWorldCoordsi().add(new Vector3i(x, y, z));
			Vector3i worldChunk = worldCoordsToChunk(new Vector3f(world.x, world.y, world.z));
			ChunkData chunk = getChunkAtPos(worldChunk.x, worldChunk.y, worldChunk.z);
			if(chunk != null ) {
				Vector3i chunkCoord = chunk.getWorldCoordsi();
				System.out.println(world);
				world.sub(chunkCoord);
				Block block = chunk.getBlock(world.x, world.y, world.z);
				System.out.println(world);
				if(block != null) {
					return block.isTransparent();
				}
			}
		}
		return true;
	}
	
	public ChunkData getChunkAtPos(int x, int y, int z) {
		for(Entry<ChunkData, GameObject> entry : renderedChunks.entrySet()) {
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
		renderedChunks.put(chunk, obj);
		Cubulus.getInstance().runLater(() -> {
			Vector3f pos = chunk.getWorldCoords();
			obj.updateMesh(MeshChunk.generateMeshForChunk(chunk, new Texture("cubulus:texture/block/block_stone.png")));
			obj.setPosition(pos.x, pos.y, pos.z);
			GameLogicCore.getInstance().addGameObject(obj);
		});
	}
	
}