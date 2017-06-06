package com.cjburkey.cubulus.block;

import java.util.ArrayList;
import java.util.List;
import com.cjburkey.cubulus.Cubulus;

public final class Blocks {
	
	private static final List<Block> blocks = new ArrayList<>();
	
	public static Block addBlock(Block block) {
		blocks.add(block);
		Cubulus.info("Added block: " + block.getBlockName() + ". Texture: " + block.getTextureLocation());
		return block;
	}
	
	public static Block blockStone;
	
	public static void initBlocks() {
		Cubulus.info("Initializing blocks...");
		blockStone = addBlock(new Block("block_stone", "cubulus:texture/block/block_stone.png", true));
		Cubulus.info("Initialized blocks.");
	}
	
	public static Block[] getAllBlocks() {
		return blocks.toArray(new Block[blocks.size()]);
	}
	
}