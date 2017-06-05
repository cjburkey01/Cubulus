package com.cjburkey.cubulus.logic;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.GameStateHandler;
import com.cjburkey.cubulus.Utils;
import com.cjburkey.cubulus.event.EventHandler;
import com.cjburkey.cubulus.input.KeyboardHandler;
import com.cjburkey.cubulus.input.MouseHandler;
import com.cjburkey.cubulus.object.GameItem;
import com.cjburkey.cubulus.object.Mesh;
import com.cjburkey.cubulus.object.MeshTestCube;
import com.cjburkey.cubulus.render.Renderer;
import com.cjburkey.cubulus.window.Window;

public final class GameLogicCore implements IGameLogic {
	
	private Renderer renderer;
	private List<GameItem> gameItems = new ArrayList<>();
	private final Vector3f cameraInc = new Vector3f();
	private final float CAM_MOVE_SPEED = 0.1f;
	private final float CAM_ROT_SPEED = 0.25f;
	
	public GameLogicCore() {
		spawnMeshes(true);
	}
	
	private void spawnMeshes(boolean clear) {
		if(clear) {
			gameItems.clear();
		}
		Mesh textured = new MeshTestCube("/texture/stone.png");
		Mesh colored = new MeshTestCube(new Vector3f(0.5f, 0.6f, 0.7f));
		for(int i = 0; i < 10; i ++) {
			spawnMeshInstance((Utils.randomRangei(0, 1, true) == 1) ? textured : colored);
		}
	}
	
	private void spawnMeshInstance(Mesh mesh) {
		GameItem item = new GameItem(mesh);
		item.setPosition(Utils.randomRangef(-10.0f, 10.0f, true), 0.0f, Utils.randomRangef(-10.0f, 10.0f, true));
		gameItems.add(item);
	}
	
	public void onGameUpdate() {
		if(renderer != null && renderer.getCamera() != null) {
			for(GameItem item : gameItems) {
				item.getRotation().y += 0.5f;
			}
			processInput(Cubulus.getGameWindow(), Cubulus.getGameWindow().getInput().getMouseHandler(), Cubulus.getGameWindow().getInput().getKeyboardHandler());
			renderer.getCamera().move(cameraInc.x * CAM_MOVE_SPEED, cameraInc.y * CAM_MOVE_SPEED, cameraInc.z * CAM_MOVE_SPEED);
			Vector2f rot = Cubulus.getGameWindow().getInput().getMouseHandler().getDisplayVector();
			renderer.getCamera().rotate(rot.x * CAM_ROT_SPEED, rot.y * CAM_ROT_SPEED, 0);
			renderer.getCamera().getRotation().x = Utils.clamp(renderer.getCamera().getRotation().x, -90, 90);
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
	
	public void onGameInit() {
		Cubulus.info("Initialized core game.");
		GameStateHandler.unPause();
		Cubulus.getInstance().getEventHandler().addListener(EventHandler.KEY_DOWN_EVENT, (data) -> onKeyDown(data.getLong("window"), data.getInt("key")));
		Cubulus.getGameWindow().getInput().getMouseHandler().init(Cubulus.getGameWindow());
	}
	
	public void onRenderUpdate() {
		renderer.render(gameItems.toArray(new GameItem[gameItems.size()]));
	}
	
	public void onRenderInit() {
		renderer = new Renderer();
		renderer.init();
	}
	
	public void onRenderCleanup() {
		for(GameItem item : gameItems) {
			item.getMesh().cleanUp();
		}
		renderer.cleanup();
	}
	
	public void onGameCleanup() {
		gameItems.clear();
	}
	
	public void onKeyDown(long window, int key) {
		if(key == GLFW.GLFW_KEY_KP_ENTER) {
			Cubulus.getInstance().closeGame();
			return;
		}
		if(key == GLFW.GLFW_KEY_R) {
			spawnMeshes(true);
			return;
		}
		if(key == GLFW.GLFW_KEY_F) {
			spawnMeshes(false);
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
	
	/*private void asciiPrint() {
		Cubulus.info("Printing some fun ascii text:");
		String file = "/ascii.txt";
		try {
			Scanner scanner = new Scanner(getClass().getResourceAsStream(file));
			List<String> output = new ArrayList<>();
			while(scanner.hasNextLine()) {
				output.add(scanner.nextLine());
			}
			scanner.close();
			for(String s : output) {
				System.out.println("\t" + s);
			}
		} catch(Exception e) {
			Cubulus.getInstance().error(-2, false, e);
		}
	}*/
	
}