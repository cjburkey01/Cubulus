package com.cjburkey.cubulus.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseHandler {
	
	private final Vector2d prevPos;
	private final Vector2d pos;
	private final Vector2f displVec;
	private boolean inWindow = true;
	private boolean leftPressed = false;
	private boolean middlePressed = false;
	private boolean rightPressed = false;
	
	public MouseHandler() {
		prevPos = new Vector2d(-1.0d, -1.0d);
		pos = new Vector2d(0, 0);
		displVec = new Vector2f();
	}
	
	public void init(long window) {
		GLFW.glfwSetCursorPosCallback(window, (w, x, y) -> {
			pos.x = x;
			pos.y = y;
		});
		
		GLFW.glfwSetMouseButtonCallback(window, (w, button, action, mode) -> {
			leftPressed = (button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS);
			rightPressed = (button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS);
			middlePressed = (button == GLFW.GLFW_MOUSE_BUTTON_3 && action == GLFW.GLFW_PRESS);
		});
		
		GLFW.glfwSetCursorEnterCallback(window, (w, entered) -> inWindow = entered);
	}
	
	public void update(long window) {
		displVec.x = 0;
		displVec.y = 0;
		if(prevPos.x > 0 && prevPos.y > 0 && inWindow) {
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
		}
		prevPos.x = pos.x;
		prevPos.y = pos.y;
	}
	
	public Vector2f getDisplayVector() {
		return displVec;
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