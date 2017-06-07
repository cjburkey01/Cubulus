package com.cjburkey.cubulus.chunk;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.logic.GameLogicCore;
import com.cjburkey.cubulus.world.ChunkGeneration;

public class ChunkHandler {
	
	private static final boolean VERBOSE = false;
	
	private final int updateChunkChecksPerSecond = 50;
	private final double updateBetweenChunkChecks = 1000000000d / (double) updateChunkChecksPerSecond;
	private long updateLastChunkCheck = System.nanoTime();
	
	private final int renderChunkChecksPerSecond = 50;
	private final double renderBetweenChunkChecks = 1000000000d / (double) renderChunkChecksPerSecond;
	private long renderLastChunkCheck = System.nanoTime();
	
	private final Queue<ChunkData> generatedChunks;
	private final Queue<GameObjectChunk> renderedChunks;
	
	public ChunkHandler() {
		generatedChunks = new ConcurrentLinkedQueue<>();
		renderedChunks = new ConcurrentLinkedQueue<>();
	}
	
	public GenerationStatus update(Vector3f around, int radius) {
		long now = System.nanoTime();
		if(now - updateLastChunkCheck >= updateBetweenChunkChecks) {
			updateLastChunkCheck = now;
			for(int x = -radius; x <= radius; x ++) {
				for(int y = -radius; y <= radius; y ++) {
					for(int z = -radius; z <= radius; z ++) {
						Vector3i posChunk = worldCoordsToChunk(around);
						if(generateChunk(posChunk.add(x, y, z))) {
							if(VERBOSE) {
								Cubulus.info("Generated: " + posChunk);
							}
							return GenerationStatus.GENERATED;
						}
					}
				}
			}
			return GenerationStatus.DONE;
		}
		return GenerationStatus.WAITING;
	}
	
	public RenderStatus render(Vector3f around, int createDistance, int destroyDistance) {
		long now = System.nanoTime();
		final int chunkSize = ChunkData.getChunkSize();
		if(now - renderLastChunkCheck >= renderBetweenChunkChecks) {
			renderLastChunkCheck = now;
			for(ChunkData chunk : generatedChunks) {
				Vector3f centerOfChunk = new Vector3f(chunk.getWorldCoordsf()).add(chunkSize / 2, chunkSize, chunkSize / 2);
				float distance = Utils.distance(centerOfChunk, around);
				if(distance <= createDistance * chunkSize) {
					if(renderChunk(chunk)) {
						if(VERBOSE) {
							Cubulus.info("Rendered: " + chunk.getChunkCoords());
						}
						return RenderStatus.RENDERED;
					}
				} else if(distance >= destroyDistance * chunkSize) {
					if(deRenderChunk(chunk)) {
						if(VERBOSE) {
							Cubulus.info("De-Rendered: " + chunk.getChunkCoords());
						}
					}
				}
			}
			return RenderStatus.DONE;
		}
		return RenderStatus.WAITING;
	}
	
	public void gameCleanup() {
		generatedChunks.clear();
	}
	
	public void renderCleanup() {
		renderedChunks.clear();
	}
	
	private boolean generateChunk(Vector3i pos) {
		ChunkData chunk = getGeneratedChunkAtChunkPos(pos.x, pos.y, pos.z);
		if(chunk == null) {
			chunk = new ChunkData(pos.x, pos.y, pos.z);
			ChunkGeneration.generateChunk(chunk);
			return generatedChunks.add(chunk);
		}
		return false;
	}
	
	private boolean renderChunk(ChunkData chunk) {
		if(chunk != null) {
			if(getRenderedChunkAtChunkPos(chunk.getChunkCoords()) == null) {
				GameObjectChunk render = GameObjectChunk.createGameObject(chunk);
				return renderedChunks.add(render);
			}
		}
		return false;
	}
	
	private boolean deRenderChunk(ChunkData chunk) {
		if(chunk != null) {
			GameObjectChunk at = getRenderedChunkAtChunkPos(chunk.getChunkCoords());
			if(at != null) {
				renderedChunks.remove(at);
				GameLogicCore.getInstance().removeGameObject(at);
				return true;
			}
		}
		return false;
	}
	
	public boolean transparentBlockAt(ChunkData data, int x, int y, int z) {
		Block at = data.getBlock(x, y, z);
		final int size = ChunkData.getChunkSize();
		if(at != null) {
			return at.isTransparent();
		} else if(x < 0 || x >= size || y < 0 || y >= size || z < 0 || z >= size) {
			Vector3i world = data.getWorldCoordsi().add(new Vector3i(x, y, z));
			Vector3i worldChunk = worldCoordsToChunk(new Vector3f(world.x, world.y, world.z));
			ChunkData chunk = getGeneratedChunkAtChunkPos(worldChunk.x, worldChunk.y, worldChunk.z);
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
	
	public ChunkData getGeneratedChunkAtChunkPos(int x, int y, int z) {
		ChunkData[] generated = generatedChunks.toArray(new ChunkData[generatedChunks.size()]);
		for(ChunkData chunk : generated) {
			Vector3i pos = chunk.getChunkCoords();
			if(pos.x == x && pos.y == y && pos.z == z) {
				return chunk;
			}
		}
		return null;
	}
	
	public GameObjectChunk getRenderedChunkAtChunkPos(int x, int y, int z) {
		GameObjectChunk[] rendered = renderedChunks.toArray(new GameObjectChunk[generatedChunks.size()]);
		for(GameObjectChunk chunk : rendered) {
			if(chunk != null) {
				Vector3i pos = chunk.getChunk().getChunkCoords();
				if(pos.x == x && pos.y == y && pos.z == z) {
					return chunk;
				}
			}
		}
		return null;
	}
	
	public GameObjectChunk getRenderedChunkAtChunkPos(Vector3i chunk) {
		return getRenderedChunkAtChunkPos(chunk.x, chunk.y, chunk.z);
	}
	
	public Vector3i worldCoordsToChunk(Vector3f world) {
		int chunkX = (int) Math.floor(world.x / (float) ChunkData.getChunkSize());
		int chunkY = (int) Math.floor(world.y / (float) ChunkData.getChunkSize());
		int chunkZ = (int) Math.floor(world.z / (float) ChunkData.getChunkSize());
		return new Vector3i(chunkX, chunkY, chunkZ);
	}
	
	public static enum GenerationStatus {
		WAITING,
		GENERATED,
		DONE,
	}
	
	public static enum RenderStatus {
		WAITING,
		RENDERED,
		DONE,
	}
	
}