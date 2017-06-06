package com.cjburkey.cubulus.chunk;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import org.joml.Vector3i;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.object.GameObjectChunk;
import com.cjburkey.cubulus.world.ChunkGeneration;

public class ChunkHandler {
	
	private final List<ChunkData> generatedChunks;
	private final List<GameObjectChunk> renderedChunks;
	
	public ChunkHandler() {
		generatedChunks = new ArrayList<>();
		renderedChunks = new ArrayList<>();
	}
	
	/**
	 * Generates a chunk in the world using chunk coordinates, but only if it is not yet generated.
	 * 
	 * @param	x	The X Chunk coordinate
	 * @param	y	The Y Chunk coordinate
	 * @param	z	The Z Chunk coordinate
	 * @return	Whether or not the chunk was generated.
	 */
	public boolean generateChunk(int x, int y, int z) {
		if(getGeneratedChunkAtChunkPos(x, y, z) == null) {
			ChunkData chunk = new ChunkData(x, y, z);
			ChunkGeneration.generateChunk(chunk);
			boolean worked = generatedChunks.add(chunk);
			return worked;
		}
		return false;
	}
	
	/**
	 * Renders a chunk in the world using chunk coordinates, but only if it is not yet rendered.
	 * 
	 * @param	x	The X Chunk coordinate
	 * @param	y	The Y Chunk coordinate
	 * @param	z	The Z Chunk coordinate
	 * @return	Whether or not the chunk was generated.
	 */
	public boolean renderChunk(int x, int y, int z) {
		ChunkData at = getGeneratedChunkAtChunkPos(x, y, z);
		if(at != null) {
			GameObjectChunk atRendered = getRenderedChunkAtChunkPos(x, y, z);
			if(atRendered == null) {
				GameObjectChunk atChunk = new GameObjectChunk(at);
				atChunk.setPosition(at.getWorldCoordsi().x, at.getWorldCoordsi().y, at.getWorldCoordsi().z);
				renderedChunks.add(atChunk);
				Cubulus.getInstance().runLater(() -> atChunk.render());
				System.out.println("Render chunk");
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
	
	public ChunkData getGeneratedChunkAtWorldPos(Vector3f world) {
		Vector3i chunk = worldCoordsToChunk(world);
		return getGeneratedChunkAtChunkPos(chunk.x, chunk.y, chunk.z);
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
	
	public Vector3i worldCoordsToChunk(Vector3f world) {
		int chunkX = (int) Math.floor(world.x / (float) ChunkData.getChunkSize());
		int chunkY = (int) Math.floor(world.y / (float) ChunkData.getChunkSize());
		int chunkZ = (int) Math.floor(world.z / (float) ChunkData.getChunkSize());
		return new Vector3i(chunkX, chunkY, chunkZ);
	}
	
}