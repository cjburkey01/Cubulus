package com.cjburkey.cubulus.logic;

import com.cjburkey.cubulus.Cubulus;

public class GameLogicCore implements IGameLogic {
	
	public boolean init() {
		Cubulus.getInstance().getLogger().info("Initialized core game.");
		return true;
	}
	
	public boolean update() {
		return false;
	}
	
	public boolean render() {
		return false;
	}
	
}