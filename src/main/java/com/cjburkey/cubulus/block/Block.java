package com.cjburkey.cubulus.block;

public class Block {
	
	private String blockName;
	private String textureLoc;
	
	public Block(String unlocName, String texture) {
		this.blockName = unlocName;
		this.textureLoc = texture;
	}
	
	public String getBlockName() {
		return blockName;
	}
	
	public String getTextureLocation() {
		return textureLoc;
	}
	
}