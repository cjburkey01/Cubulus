package com.cjburkey.cubulus.chunk;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.object.GameObject;
import com.cjburkey.cubulus.object.Mesh;
import com.cjburkey.cubulus.render.Texture;
import com.cjburkey.cubulus.world.ChunkGeneration;


// 
// TODO: NECESITAS OPTIMIZE THIS MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// TODO: MUY MUCHO
// 
public class ChunkHandler {
	
	private final Map<ChunkData, GameObject> renderedChunks;
	private final Stack<Vector3i> chunksToDo;
	private Thread processingThread;
	private boolean running = false;
	private int chunksPerSecond = Integer.MAX_VALUE - 100;
	private final int loadedChunksPs = 25;
	private int runsNeeded = 0;
	
	public ChunkHandler() {
		renderedChunks = new ConcurrentHashMap<>();
		chunksToDo = new Stack<>();
	}
	
	private void start() {
		if(!running) {
			running = true;
			processingThread = new Thread(() -> {
				Cubulus.info("Starting chunk generation.");
				double betweenRuns;
				long lastRun = System.nanoTime();
				long runs = 0;
				while(running) {
					betweenRuns = 1000000000d / (double) chunksPerSecond;
					long now = System.nanoTime();
					if(now - lastRun >= betweenRuns) {
						runs ++;
						lastRun = now;
						processNextChunk();
						if(runs >= runsNeeded && chunksPerSecond != loadedChunksPs) {
							chunksPerSecond = loadedChunksPs;
						}
					}
				}
				chunksToDo.clear();
				Cubulus.info("Stopping chunk generation.");
			});
			processingThread.setName("GenerationThread");
			processingThread.start();
		}
	}
	
	public void stop() {
		running = false;
	}
	
	public void addChunk(int x, int y, int z) {
		if(!running) {
			Cubulus.getInstance().runLater(() -> start());
		}
		Vector3i vec = new Vector3i(x, y, z);
		if(getChunkAtPos(x, y, z) == null && !chunksToDo.contains(vec)) {
			chunksToDo.add(vec);
		}
	}
	
	public void removeChunk(int x, int y, int z) {
		ChunkData chunk = getChunkAtPos(x, y, z);
		if(chunk != null) {
			GameLogicCore.getInstance().removeGameObject(renderedChunks.get(chunk));
			renderedChunks.remove(chunk);
		}
	}
	
	public void ensureChunksAround(Vector3f worldCoords, int chunkRadius) {
		int size = ChunkData.getChunkSize();
		Vector3f min = new Vector3f(worldCoords.x - chunkRadius * size, worldCoords.y - chunkRadius * size, worldCoords.z - chunkRadius * size);
		Vector3f max = new Vector3f(worldCoords.x + chunkRadius * size, worldCoords.y + chunkRadius * size, worldCoords.z + chunkRadius * size);
		removeExtraChunks(min, max);
		runsNeeded = (int) Math.pow(chunkRadius * 2 + 1, 3);
		for(int x = -chunkRadius; x <= chunkRadius; x ++) {
			for(int y = -chunkRadius; y <= chunkRadius; y ++) {
				for(int z = -chunkRadius; z <= chunkRadius; z ++) {
					Vector3i chunkCoords = worldCoordsToChunk(worldCoords);
					chunkCoords.add(x, y, z);
					addChunk(chunkCoords.x, chunkCoords.y, chunkCoords.z);
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
				world.sub(chunkCoord);
				Block block = chunk.getBlock(world.x, world.y, world.z);
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
	
	public void redrawNeighbors(ChunkData chunk) {
		Vector3i self = chunk.getChunkCoords();
		
		ChunkData left = getChunkAtPos(self.x - 1, self.y, self.z);
		ChunkData right = getChunkAtPos(self.x + 1, self.y, self.z);
		ChunkData down = getChunkAtPos(self.x, self.y - 1, self.z);
		ChunkData up = getChunkAtPos(self.x, self.y + 1, self.z);
		ChunkData forward = getChunkAtPos(self.x, self.y + 1, self.z - 1);
		ChunkData back = getChunkAtPos(self.x, self.y + 1, self.z + 1);
		
		redrawChunk(left);
		redrawChunk(right);
		redrawChunk(down);
		redrawChunk(up);
		redrawChunk(forward);
		redrawChunk(back);
	}
	
	public Vector3i worldCoordsToChunk(Vector3f world) {
		int chunkX = (int) Math.floor(world.x / (float) ChunkData.getChunkSize());
		int chunkY = (int) Math.floor(world.y / (float) ChunkData.getChunkSize());
		int chunkZ = (int) Math.floor(world.z / (float) ChunkData.getChunkSize());
		return new Vector3i(chunkX, chunkY, chunkZ);
	}
	
	private void removeExtraChunks(Vector3f min, Vector3f max) {
		for(Entry<ChunkData, GameObject> entry : renderedChunks.entrySet()) {
			Vector3f pos = entry.getKey().getWorldCoords();
			int size = ChunkData.getChunkSize();
			if(pos.x + 2 * size < min.x || pos.x - 2 * size > max.x || pos.y + 2 * size < min.y || pos.y - 2 * size > max.y || pos.z + 2 * size < min.z || pos.z - 2 * size > max.z) {
				GameLogicCore.getInstance().removeGameObject(entry.getValue());
				renderedChunks.remove(entry.getKey());
			}
		}
	}
	
	private void redrawChunk(ChunkData chunk) {
		if(chunk != null) {
			GameObject obj = renderedChunks.get(chunk);
			if(obj != null) {
				Cubulus.getInstance().runLater(() -> {
					Cubulus.info("Redrawing chunk: " + chunk.getChunkCoords());
					obj.getMesh().cleanUp();
					Mesh m = getChunkMesh(chunk);
					m.build();
					obj.updateMesh(m);
				});
			}
		}
	}
	
	private Mesh getChunkMesh(ChunkData chunk) {
		return MeshChunk.generateMeshForChunk(chunk, new Texture("cubulus:texture/block/block_stone.png"));
	}
	
	private void spawnMesh(ChunkData chunk) {
		GameObject obj = new GameObject(null);
		renderedChunks.put(chunk, obj);
		Vector3f pos = chunk.getWorldCoords();
		obj.setPosition(pos.x, pos.y, pos.z);
		GameLogicCore.getInstance().addGameObject(obj);
		Cubulus.getInstance().runLater(() -> {
			Mesh m = getChunkMesh(chunk);
			m.build();
			obj.updateMesh(m);
		});
	}
	
	private boolean processNextChunk() {
		if(chunksToDo.size() > 0) {
			Vector3i cPos = chunksToDo.pop();
			if(getChunkAtPos(cPos.x, cPos.y, cPos.z) == null) {
				ChunkData chunk = new ChunkData(cPos.x, cPos.y, cPos.z);
				ChunkGeneration.generateChunk(chunk);
				spawnMesh(chunk);
				redrawNeighbors(chunk);
				return true;
			}
		}
		return false;
	}
	
}