package com.cjburkey.cubulus.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.input.KeyboardHandler;

public final class Window {
	
	private long window;
	private boolean resized;
	private int width, height;
	
	public Window(int width, int height, String title, boolean vsync) {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!GLFW.glfwInit()) {
			Cubulus.getInstance().error(-1, true, "GLFW could not be initialized. Failed to launch.");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);           
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		
		window = GLFW.glfwCreateWindow(5, 5, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(window == MemoryUtil.NULL) {
			Cubulus.getInstance().error(-1, true, "GLFW window could not be created. Failed to launch.");
		}
		setSize(true, width, height);
		
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> KeyboardHandler.keyChanged(window, key, action));
		GLFW.glfwSetFramebufferSizeCallback(window, (window, w, h) -> setSize(false, w, h));
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval((vsync) ? 1 : 0);
		GLFW.glfwShowWindow(window);
	}
	
	public void setSize(boolean center, int width, int height) {
		this.width = width;
		this.height = height;
		GLFW.glfwSetWindowSize(window, width, height);
		if(center) {
			centerWindow();
		}
		resized = true;
	}
	
	public void cancelResize() {
		resized = false;
	}
	
	public void centerWindow() {
		GLFW.glfwSetWindowPos(window, (getMonitorWidth() - getWidth()) / 2, (getMonitorHeight() - getHeight()) / 2);
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(window, title);
	}
	
	public long getWindow() {
		return window;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isResized() {
		return resized;
	}
	
	public int getMonitorWidth() {
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		return vidmode.width();
	}
	
	public int getMonitorHeight() {
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		return vidmode.height();
	}
	
}