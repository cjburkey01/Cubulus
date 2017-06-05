package com.cjburkey.cubulus.core;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.input.MouseHandler;
import com.cjburkey.cubulus.window.Window;

public final class GameStateHandler {
	
	private static boolean paused = false;
	
	public static void unPause() {
		paused = false;
		resetCursorPosition();
	}
	
	public static void pause() {
		paused = true;
		resetCursorPosition();
	}
	
	public static boolean isPaused() {
		return paused;
	}
	
	public static void resetCursorPosition() {
		Window w = Cubulus.getGameWindow();
		MouseHandler mh = w.getInput().getMouseHandler();
		
		Vector2d pos = new Vector2d(w.getWidth() / 2, w.getHeight() / 2);
		GLFW.glfwSetCursorPos(w.getWindow(), pos.x, pos.y);
		mh.getPreviousPosition().x = pos.x;
		mh.getPosition().x = pos.x;
		mh.getPreviousPosition().y = pos.y;
		mh.getPosition().y = pos.y;
	}
	
}