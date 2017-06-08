package com.cjburkey.cubulus.lwjgl;

import org.joml.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class GLFWWindow {
	
	private int width;
	private int height;
	private boolean resized = false;
	private long window = 0;
	
	public void create(double ratio) {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("GLFW window could not be initialized.");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		
		window = GLFW.glfwCreateWindow(20, 20, "Cubulus", MemoryUtil.NULL, MemoryUtil.NULL);
		Vector2i size = getScreenSize();
		
		setWindowSize((int) ((double) size.x / ratio), (int) ((double) size.y / ratio));
		centerOnScreen();
		
		if(window == MemoryUtil.NULL) {
			throw new RuntimeException("GLFW Window could not be created");
		}
		
		GLFW.glfwSetWindowSizeCallback(window, (wid, width, height) -> setWindowSize(width, height));
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
	}
	
	public void centerOnScreen() {
		Vector2i monitor = getScreenSize();
		GLFW.glfwSetWindowPos(window, (monitor.x - width) / 2, (monitor.y - height) / 2);
	}
	
	public void show() {
		GLFW.glfwShowWindow(window);
		GL.createCapabilities();
	}
	
	public void render() {
		if(resized) {
			GL11.glViewport(0, 0, width, height);
			resized = false;
		}
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public void cleanup() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	public void setWindowSize(int width, int height) {
		this.width = width;
		this.height = height;
		GLFW.glfwSetWindowSize(window, width, height);
		resized = true;
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public long getId() {
		return window;
	}
	
	public Vector2i getWindowSize() {
		return new Vector2i(width, height);
	}
	
	public Vector2i getScreenSize() {
		Vector2i size = new Vector2i();
		try(MemoryStack stack = MemoryStack.stackPush()) {
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			size.set(vidMode.width(), vidMode.height());
		}
		return size;
	}
	
}