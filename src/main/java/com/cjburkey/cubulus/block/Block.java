package com.cjburkey.cubulus.block;

public class Block {
	
	private final String blockName;
	private final String textureLoc;
	private final boolean fullBlock;
	
	public Block(String unlocName, String texture, boolean fullBlock) {
		this.blockName = unlocName;
		this.textureLoc = texture;
		this.fullBlock = fullBlock;
	}
	
	public Block(String unlocName, String texture) {
		this.blockName = unlocName;
		this.textureLoc = texture;
		fullBlock = true;
	}
	
	public String getBlockName() {
		return blockName;
	}
	
	public String getTextureLocation() {
		return textureLoc;
	}
	
	public boolean isTransparent() {
		return !fullBlock;
	}
	
}