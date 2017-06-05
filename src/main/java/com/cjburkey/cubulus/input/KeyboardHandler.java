package com.cjburkey.cubulus.input;

import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.event.EventData;
import com.cjburkey.cubulus.event.EventHandler;
import com.cjburkey.cubulus.window.Window;

public final class KeyboardHandler {
	
	public boolean keyHeld(Window window, int key) {
		return GLFW.glfwGetKey(window.getWindow(), key) == GLFW.GLFW_PRESS;
	}
	
	public void keyChanged(long window, int key, int action) {
		EventData data = new EventData();
		data.set("window", window);
		data.set("key", key);
		if(action == GLFW.GLFW_PRESS) {
			Cubulus.getInstance().getEventHandler().publishEvent(EventHandler.KEY_DOWN_EVENT, data);
		} else if(action == GLFW.GLFW_RELEASE) {
			Cubulus.getInstance().getEventHandler().publishEvent(EventHandler.KEY_UP_EVENT, data);
		} else return;
	}
	
}