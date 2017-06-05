package com.cjburkey.cubulus.logic;

public interface IGameLogic {
	
	void onGameUpdate();
	void onRenderUpdate();
	void onGameInit();
	void onRenderInit();
	void onGameCleanup();
	void onRenderCleanup();
	
}