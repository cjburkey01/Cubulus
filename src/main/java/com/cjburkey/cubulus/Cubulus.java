package com.cjburkey.cubulus;

import org.joml.Vector3i;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;
import com.cjburkey.cubulus.coremodule.CoreModuleHandler;
import com.cjburkey.cubulus.loop.GameLoop;
import com.cjburkey.cubulus.loop.RenderLoop;
import com.cjburkey.cubulus.lwjgl.GLFWWindow;

public final class Cubulus {
	
	private static final Vector3i version = new Vector3i(0, 0, 1);
	
	private final Thread mainThread;
	private final Logger logger;
	private final CoreModuleHandler moduleHandler;
	
	private GLFWWindow window;
	private RenderLoop renderLoop;
	private GameLoop gameLoop;
	
	public static void main(String[] args) {
		(instance = new Cubulus()).init();
		info("Closed game.");
	}
	
	private Cubulus() {
		logger = new Logger("[Cubulus-%s] %s");
		moduleHandler = new CoreModuleHandler();
		mainThread = Thread.currentThread();
	}
	
	private void init() {
		moduleHandler.findAndLoadModules();
		initWindow();
		infoDump();
		startLoops();
	}
	
	private void initWindow() {
		window = new GLFWWindow();
		window.create(1.2d);
		window.show();
		coreGlfwInit();
	}
	
	private void infoDump() {
		System.out.println();
		logger.info("Dumping information:");
		logger.info("  Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		logger.info("  LWJGL: " + Version.getVersion());
		logger.info("  OpenGL: " + GL11.glGetString(GL11.GL_VERSION));
		logger.info("  Cubulus: v" + getVersion());
		System.out.println();
	}
	
	private void startLoops() {
		gameLoop = new GameLoop(() -> coreGameInit(), () -> tick(), () -> coreGameCleanup());
		renderLoop = new RenderLoop(() -> coreRenderInit(), () -> render(), () -> coreRenderCleanup());
		
		gameLoop.start();
		renderLoop.start();
	}
	
	private void tick() {
		coreGameTick();
	}
	
	private void render() {
		window.render();
		if(window.shouldClose()) {
			stopGame();
		}
		coreRenderUpdate();
	}
	
	public void stopGame() {
		logger.info("Closing game...");
		gameLoop.stop();
		renderLoop.stop();
		window.cleanup();
	}
	
	// -- EVENTS -- //
	
	private void coreGlfwInit() {
		moduleHandler.getEventSystem().glfwInit();
	}
	
	private void coreRenderInit() {
		moduleHandler.getEventSystem().renderInit();
	}
	
	private void coreGameInit() {
		moduleHandler.getEventSystem().gameInit();
	}
	
	private void coreRenderUpdate() {
		moduleHandler.getEventSystem().renderUpdate();
	}
	
	private void coreGameTick() {
		moduleHandler.getEventSystem().gameTick();
	}
	
	private void coreRenderCleanup() {
		moduleHandler.getEventSystem().renderCleanup();
	}
	
	private void coreGameCleanup() {
		moduleHandler.getEventSystem().gameCleanup();
	}
	
	// -- STATIC -- //
	
	private static Cubulus instance;
	
	public static Cubulus getInstance() {
		return instance;
	}
	
	public static void info(Object msg) {
		instance.logger.info(msg);
	}
	
	public static void err(Object msg) {
		instance.logger.err(msg);
	}
	
	public static boolean onRenderThread() {
		return Thread.currentThread().equals(instance.mainThread);
	}
	
	public static String getVersion() {
		return version.x + "." + version.y + "." + version.z;
	}
	
}