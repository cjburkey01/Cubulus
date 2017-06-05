package com.cjburkey.cubulus.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.core.GameStateHandler;
import com.cjburkey.cubulus.window.Window;

public final class MouseHandler {
	
	private final Vector2d prevPos;
	private final Vector2d pos;
	private final Vector2f displVec;
	private boolean leftPressed = false;
	private boolean middlePressed = false;
	private boolean rightPressed = false;
	
	public MouseHandler() {
		prevPos = new Vector2d();
		pos = new Vector2d();
		displVec = new Vector2f();
	}
	
	public void init(Window window) {
		GameStateHandler.resetCursorPosition();
		GLFW.glfwSetCursorPosCallback(window.getWindow(), (w, x, y) -> {
			if(!GameStateHandler.isPaused()) {
				pos.x = x;
				pos.y = y;
			}
		});
		
		GLFW.glfwSetMouseButtonCallback(window.getWindow(), (w, button, action, mode) -> {
			leftPressed = (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS);
			rightPressed = (button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS);
			middlePressed = (button == GLFW.GLFW_MOUSE_BUTTON_3 && action == GLFW.GLFW_PRESS);
		});
	}
	
	public void update(Window window) {
		if(GameStateHandler.isPaused()) {
			if(GLFW.glfwGetInputMode(window.getWindow(), GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_NORMAL) {
				GLFW.glfwSetInputMode(window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
				Cubulus.info("Enabling cursor.");
			}
		} else {
			if(GLFW.glfwGetInputMode(window.getWindow(), GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_DISABLED) {
				GLFW.glfwSetInputMode(window.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
				Cubulus.info("Disabling cursor.");
			}
		}
		
		displVec.x = 0;
		displVec.y = 0;
		double dX = pos.x - prevPos.x;
		double dY = pos.y - prevPos.y;
		boolean rotX = (dX != 0);
		boolean rotY = (dY != 0);
		if(rotX) {
			displVec.y = (float) dX;
		}
		if(rotY) {
			displVec.x = (float) dY;
		}
		prevPos.x = pos.x;
		prevPos.y = pos.y;
	}
	
	public Vector2f getDisplayVector() {
		return displVec;
	}
	
	public Vector2d getPosition() {
		return pos;
	}
	
	public Vector2d getPreviousPosition() {
		return prevPos;
	}
	
	public boolean isLeftPressed() {
		return leftPressed;
	}
	
	public boolean isRightPressed() {
		return rightPressed;
	}
	
	public boolean isMiddlePressed() {
		return middlePressed;
	}
	
}