package com.cjburkey.cubulus.logic;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.block.Blocks;
import com.cjburkey.cubulus.core.GameStateHandler;
import com.cjburkey.cubulus.event.EventHandler;
import com.cjburkey.cubulus.fun.AsciiMessage;
import com.cjburkey.cubulus.input.KeyboardHandler;
import com.cjburkey.cubulus.input.MouseHandler;
import com.cjburkey.cubulus.io.Dirs;
import com.cjburkey.cubulus.io.TextureAtlas;
import com.cjburkey.cubulus.light.DirectionalLight;
import com.cjburkey.cubulus.light.PointLight;
import com.cjburkey.cubulus.light.PointLight.Attenuation;
import com.cjburkey.cubulus.object.GameObject;
import com.cjburkey.cubulus.render.Camera;
import com.cjburkey.cubulus.render.Renderer;
import com.cjburkey.cubulus.window.Window;
import com.cjburkey.cubulus.world.World;

public final class GameLogicCore implements IGameLogic {
	
	private static GameLogicCore instance;
	private List<GameObject> gameObjects = new ArrayList<>();
	
	private TextureAtlas texture;
	private World world;
	private Renderer renderer;
	private final Vector3f cameraInc = new Vector3f();
	private final float CAM_MOVE_SPEED = 0.1f;
	private final float CAM_ROT_SPEED = 0.25f;
	private boolean ascii = false;
	
	private Vector3f ambientLight;
	private PointLight pointLight;
	private DirectionalLight directionalLight;
	
	public GameLogicCore() {
		instance = this;
	}
	
	public void onGameUpdate() {
		Camera cam = renderer.getCamera();
		if(renderer != null && cam != null) {
			processInput(Cubulus.getGameWindow(), Cubulus.getGameWindow().getInput().getMouseHandler(), Cubulus.getGameWindow().getInput().getKeyboardHandler());
			cam.move(cameraInc.x * CAM_MOVE_SPEED, cameraInc.y * CAM_MOVE_SPEED, cameraInc.z * CAM_MOVE_SPEED);
			Vector2f rot = Cubulus.getGameWindow().getInput().getMouseHandler().getDisplayVector();
			cam.rotate(rot.x * CAM_ROT_SPEED, rot.y * CAM_ROT_SPEED, 0);
			cam.getRotation().x = Utils.clamp(cam.getRotation().x, -90, 90);
			world.loadChunksAround(cam.getPosition(), World.loadRadius);
		}
	}
	
	public void processInput(Window window, MouseHandler mouse, KeyboardHandler keyboard) {
		cameraInc.set(0.0f, 0.0f, 0.0f);
		window.getInput().getMouseHandler().update(window);
		if(keyboard.keyHeld(window, GLFW.GLFW_KEY_W)) {
			cameraInc.z -= 1;
		}
		if(keyboard.keyHeld(window, GLFW.GLFW_KEY_S)) {
			cameraInc.z += 1;
		}
		if(keyboard.keyHeld(window, GLFW.GLFW_KEY_A)) {
			cameraInc.x -= 1;
		}
		if(keyboard.keyHeld(window, GLFW.GLFW_KEY_D)) {
			cameraInc.x += 1;
		}
		if(keyboard.keyHeld(window, GLFW.GLFW_KEY_LEFT_SHIFT)) {
			cameraInc.y -= 1;
		}
		if(keyboard.keyHeld(window, GLFW.GLFW_KEY_SPACE)) {
			cameraInc.y += 1;
		}
	}
	
	public void addGameObject(GameObject obj) {
		gameObjects.add(obj);
	}
	
	public void removeGameObject(GameObject obj) {
		Cubulus.getInstance().runLater(() -> {
			if(obj != null && obj.getMesh() != null) {
				obj.getMesh().cleanUp();
			}
		});
		gameObjects.remove(obj);
	}
	
	public void onGameInit() {
		try {
			Cubulus.info("Initialized core game.");
			if(ascii) ascii();
			Dirs.init();
			Blocks.initBlocks();
			(texture = new TextureAtlas(Blocks.getAllBlocks())).init();
			
			Cubulus.getGameWindow().getInput().getMouseHandler().resetCursorPosition();
			Cubulus.getInstance().getEventHandler().addListener(EventHandler.KEY_DOWN_EVENT, (data) -> onKeyDown(data.getLong("window"), data.getInt("key")));
			Cubulus.getGameWindow().getInput().getMouseHandler().init(Cubulus.getGameWindow());
			
			world = new World();
		} catch(Exception e) {
			Cubulus.getInstance().error(-200, true, e);
		}
	}
	
	public void onRenderUpdate() {
		renderer.render(gameObjects.toArray(new GameObject[gameObjects.size()]), ambientLight, pointLight, directionalLight);
	}
	
	public void onRenderInit() {
		ambientLight = new Vector3f(1f, 1f, 1f);
		pointLight = new PointLight(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(0, 0, 0), 100.0f, new Attenuation(1.0f, 0.0f, 0.0f));
		directionalLight = new DirectionalLight(new Vector3f(1.0f, 1.0f, 1.0f), new Vector3f(-1.0f, -1.0f, 0.0f), 100.0f);
		
		GLFW.glfwFocusWindow(Cubulus.getGameWindow().getWindow());
		renderer = new Renderer();
		renderer.init();
	}
	
	public void onRenderCleanup() {
		for(GameObject item : gameObjects) {
			if(item != null && item.getMesh() != null) {
				item.getMesh().cleanUp();
			}
		}
		renderer.cleanup();
	}
	
	public void onGameCleanup() {
		gameObjects.clear();
		texture.cleanup();
		Dirs.cleanup();
	}
	
	public void onKeyDown(long window, int key) {
		if(key == GLFW.GLFW_KEY_KP_ENTER) {
			Cubulus.getInstance().closeGame();
			return;
		}
		if(key == GLFW.GLFW_KEY_ESCAPE) {
			if(GameStateHandler.isPaused()) {
				GameStateHandler.unPause();
			} else {
				GameStateHandler.pause();
			}
			return;
		}
	}
	
	public TextureAtlas getAtlas() {
		return texture;
	}
	
	private void ascii() {
		new AsciiMessage();
	}
	
	public World getWorld() {
		return world;
	}
	
	public static GameLogicCore getInstance() {
		return instance;
	}
	
}