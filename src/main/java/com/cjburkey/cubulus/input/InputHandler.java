package com.cjburkey.cubulus.input;

public class InputHandler {
	
	private KeyboardHandler keyboard;
	private MouseHandler mouse;
	
	public InputHandler() {
		keyboard = new KeyboardHandler();
		mouse = new MouseHandler();
	}
	
	public KeyboardHandler getKeyboardHandler() {
		return keyboard;
	}
	
	public MouseHandler getMouseHandler() {
		return mouse;
	}
	
}