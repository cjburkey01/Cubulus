package com.cjburkey.cubulus.io;

import org.joml.Vector2i;
import com.cjburkey.cubulus.block.Block;

public class TextureAtlas {
	
	private int width = 1;
	private Block[] blocks;
	
	public TextureAtlas() {
		
	}
	
	public int getSize() {
		return width;
	}
	
	public Vector2i getForBlock(Block b) {
		return new Vector2i(0, 0);
	}
	
}