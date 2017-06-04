package com.cjburkey.cubulus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.reflections.Reflections;
import com.cjburkey.cubulus.core.GameLoop;
import com.cjburkey.cubulus.logic.IGameLogic;
import com.cjburkey.cubulus.window.Window;

public final class Cubulus {
	
	private boolean fullInfoDump;
	private static Cubulus instance;
	private Logger logger;
	private Window window;
	private GameLoop gameLoop;
	private List<IGameLogic> logic;
	
	public static void main(String[] args) {
		boolean log = false;
		if(args.length > 0) {
			if(args[0].trim().equals("completeInformationLog")) {
				log = true;
			}
		}
		instance = new Cubulus(log);
		instance.init();
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
	
	private void init() {
		System.out.println("Building logger...");
		logger = new Logger("[Cubulus] %s");
		logger.info("Logger built.");
		logic = new ArrayList<>();
		logger.info("Launching...");
		launch();
		logger.info("Launched.");
		logger.info("Scanning for logic...");
		findLogic();
		logger.info("Finished scan for logic.");
		infoDump();
		logger.info("Starting game loop...");
		gameLoop();
		logger.info("Finished prerequisite-initialization.");
		System.out.println("\n----------[ Begin Game ]----------\n");
		startRenderLoop();
		logger.info("Stopped.");
		System.exit(0);
	}
	
	private void findLogic() {
		try {
			Reflections reflections = new Reflections(Cubulus.class.getPackage().getName());
			Set<Class<? extends IGameLogic>> modules = reflections.getSubTypesOf(IGameLogic.class);
			for(Class<? extends IGameLogic> logic : modules) {
				IGameLogic instance = logic.newInstance();
				this.logic.add(instance);
				logger.info("  - Found: " + logic.getSimpleName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void gameLoop() {
		gameLoop = new GameLoop(60, () -> logicUpdate(), false);
		gameLoop.start();
	}
	
	private void logicInit() {
		for(IGameLogic l : logic) {
			l.init();
		}
	}
	
	private void logicRender() {
		for(IGameLogic l : logic) {
			l.render();
		}
	}
	
	private boolean firstUpdate = true;
	private void logicUpdate() {
		if(firstUpdate) {
			firstUpdate = false;
			logicInit();
		}
		for(IGameLogic l : logic) {
			l.update();
		}
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
		logicRender();
	}
	
	public static Cubulus getInstance() {
		return instance;
	}
	
}