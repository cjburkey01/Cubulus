package com.cjburkey.cubulus;

import java.util.Map.Entry;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.core.GameLoop;
import com.cjburkey.cubulus.event.EventHandler;
import com.cjburkey.cubulus.window.Window;

public final class Cubulus {
	
	private static final boolean vsync = true;	// Keep this on, if off, the game will jump a little bit every once in a while.
	
	private boolean fullInfoDump;
	private static Cubulus instance;
	private Logger logger;
	private Window window;
	private GameLoop gameLoop;
	private boolean rendering;
	private EventHandler eventHandler;
	private CoreEventDispatcher logicEvents;
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> instance.error(Short.MAX_VALUE, true, e));
		System.out.println("Set default error handling.");
		boolean log = false;
		if(args.length > 0) {
			if(args[0].trim().equals("completeInformationLog")) {
				log = true;
			}
		}
		instance = new Cubulus(log);
		instance.start();
	}
	
	public Cubulus(boolean fullLog) {
		fullInfoDump = fullLog;
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
	
	public Logger getLogger() {
		return logger;
	}
	
	public Window getWindow() {
		return window;
	}
	
	public EventHandler getEventHandler() {
		return eventHandler;
	}
	
	private void start() {
		long start = System.currentTimeMillis();
		System.out.println("Building logger...");
		logger = new Logger("[Cubulus] %s");
		logger.info("Logger built.");
		logicEvents = new CoreEventDispatcher();
		eventHandler = new EventHandler();
		logger.info("Launching...");
		launch();
		logger.info("Launched.");
		logger.info("Scanning for logic...");
		findLogic();
		logger.info("Finished scan for logic.");
		infoDump();
		long now = System.currentTimeMillis();
		logger.info("Finished prerequisite-initialization. Took: " + (now - start) + "ms.");
		finish();
	}
	
	private void finish() {
		System.out.println("\n----------[ Begin Game ]----------\n");
		logicEvents.gameInit();
		startRenderLoop();
		logger.info("Render loop closed.");
		logger.info("Stopped.");
		System.exit(0);
	}
	
	private void findLogic() {
		try {
			logicEvents.findLogic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void gameLoop() {
		gameLoop = new GameLoop("UpdateLoop Thread", 60, () -> logicEvents.gameUpdate(), false);
		gameLoop.start();
	}
	
	public void closeGame() {
		logger.info("Calmly closing game...");
		gameLoop.stop();
		rendering = false;
	}
	
	private void infoDump() {
		logger.info("INFO DUMP:");
		if(!fullInfoDump) {
			logger.info("  - Operating System: " + System.getProperty("os.name"));
			logger.info("  - Operating System Version: " + System.getProperty("os.version"));
			logger.info("  - Java Version: " + System.getProperty("java.version"));
		} else {
			for(Entry<Object, Object> entry : System.getProperties().entrySet()) {
				logger.info("  - " + entry.getKey() + ": " + entry.getValue());
			}
		}
		logger.info("  - LWJGL Version: " + Version.getVersion());
		logger.info("  - OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		logger.info("  - Cubulus Version: " + Info.getGameVersion());
	}
	
	private void launch() {
		window = new Window(300, 300, "Cubulus v" + Info.getGameVersion(), vsync);
		window.setSize(true, window.getMonitorWidth() * 2 / 3, window.getMonitorHeight() * 2 / 3);
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	private int fps = 0;
	private int frames = 0;
	private long lastFps = System.nanoTime();
	private void startRenderLoop() {
		rendering = true;
		logicEvents.renderInit();
		logger.info("Starting game loop...");
		gameLoop();
		while(rendering) {
			if(GLFW.glfwWindowShouldClose(window.getWindow())) {
				closeGame();
			}
			renderLoop();
			frames++;
			long now = System.nanoTime();
			if(now - lastFps >= 1000000000) {
				fps = frames;
				frames = 0;
				lastFps = now;
			}
			try { Thread.sleep(1); } catch(Exception e) { System.exit(-134); }
		}
		logger.info("Cleaning up GLFW...");
		GLFW.glfwDestroyWindow(window.getWindow());
		logger.info("Cleaned up GLFW.");
		logicEvents.renderCleanup();
		logger.info("Stopping...");
	}
	
	private void renderLoop() {
		if(window.isResized()) {
			window.cancelResize();
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
		}
		setBuiltWindowTitle();
		logicEvents.renderUpdate();
		GLFW.glfwSwapBuffers(window.getWindow());
		GLFW.glfwPollEvents();
	}
	
	private void setBuiltWindowTitle() {
		int ups = gameLoop.getUpdatesPerSecond();
		window.setTitle("Cubulus v" + Info.getGameVersion() + " (" + window.getWidth() + "x" + window.getHeight() + ") [FPS: " + fps + ", UPS: " + ups + "]");
	}
	
	public static Cubulus getInstance() {
		return instance;
	}
	
	public static Window getGameWindow() {
		return instance.window;
	}
	
	public static void info(Object msg) {
		instance.logger.info(msg);
	}
	
	public static CoreEventDispatcher getCoreEventDispatcherForLogicEvents() {
		return instance.logicEvents;
	}
	
	/*private void logicGameInit() {
		window.getInput().getMouseHandler().init(window.getWindow());
		for(IGameLogic l : logic) {
			l.onGameInit();
		}
	}
	
	private void logicRenderInit() {
		for(IGameLogic l : logic) {
			l.onRenderInit();
		}
	}
	
	private boolean firstUpdate = true;
	private void logicUpdate() {
		if(firstUpdate) {
			firstUpdate = false;
			logicGameInit();
		}
		window.getInput().getMouseHandler().update();
		for(IGameLogic l : logic) {
			l.onUpdate();
		}
	}
	
	private boolean firstRender = true;
	private void logicRender() {
		if(firstRender) {
			firstRender = false;
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			logicRenderInit();
		}
		for(IGameLogic l : logic) {
			l.onRender();
		}
	}
	
	private void logicCleanup() {
		
		for(IGameLogic l : logic) {
			l.onCleanup();
		}
	}*/
	
}