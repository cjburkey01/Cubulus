package com.cjburkey.cubulus;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.core.GameLoop;
import com.cjburkey.cubulus.window.Window;

public final class Cubulus {
	
	private static Cubulus instance;
	private Logger logger;
	private Window window;
	private GameLoop gameLoop;
	
	public static void main(String[] args) {
		instance = new Cubulus();
		instance.init();
	}
	
	private void init() {
		System.out.println("Building logger...");
		logger = new Logger("[Cubulus] %s");
		logger.info("Logger built.");
		logger.info("Launching...");
		launch();
		logger.info("Launched.");
		infoDump();
		logger.info("Starting game loop.");
		gameLoop();
		logger.info("Starting render loop.");
		startRenderLoop();
		logger.info("Stopped.");
		System.exit(0);
	}

	private void gameLoop() {
		gameLoop = new GameLoop(60, () -> update());
		gameLoop.start();
	}
	
	private void render() {
		
	}
	private void update() {
		
	}
	
	private void infoDump() {
		logger.info("INFO DUMP:");
		logger.info("  - Operating System: " + System.getProperty("os.name"));
		logger.info("  - Operating System Version: " + System.getProperty("os.version"));
		logger.info("  - LWJGL Version: " + Version.getVersion());
		logger.info("  - OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		logger.info("  - Cubulus Version: " + Info.getGameVersion());
	}
	
	public void error(int code, boolean fatal, Throwable t) {
		logger.err(t.getMessage());
		t.printStackTrace();
		if(fatal) System.exit(code);
	}
	
	public void error(int code, boolean fatal, String msg) {
		logger.err(msg);
		if(fatal) System.exit(code);
	}
	
	private void launch() {
		window = new Window(300, 300, "Cubulus v" + Info.getGameVersion(), true);
		GL.createCapabilities();
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
	}
	
	private void startRenderLoop() {
		while(!GLFW.glfwWindowShouldClose(window.getWindow())) {
			renderLoop();
		}
		logger.info("Stopping...");
	}
	
	private void renderLoop() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GLFW.glfwSwapBuffers(window.getWindow());
		GLFW.glfwPollEvents();
		render();
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public static Cubulus getInstance() {
		return instance;
	}
	
}