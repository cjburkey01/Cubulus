package com.cjburkey.cubulus.core;

import com.cjburkey.cubulus.Cubulus;

public final class GameStateHandler {
	
	private static boolean paused = false;
	
	public static void unPause() {
		paused = false;
		Cubulus.getGameWindow().getInput().getMouseHandler().resetCursorPosition();
	}
	
	public static void pause() {
		paused = true;
		Cubulus.getGameWindow().getInput().getMouseHandler().resetCursorPosition();
	}
	
	public static boolean isPaused() {
		return paused;
	}
	
}