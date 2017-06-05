package com.cjburkey.cubulus.logic;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.event.EventHandler;
import com.cjburkey.cubulus.object.GameItem;
import com.cjburkey.cubulus.object.Mesh;
import com.cjburkey.cubulus.render.Renderer;

public final class GameLogicCore implements IGameLogic {
	
	private Renderer renderer;
	private List<GameItem> gameItems = new ArrayList<>();
	
	public GameLogicCore() {
		gameItems.add(new GameItem(new Mesh(new float[] {
				-0.5f, 0.5f, -1.05f,
				-0.5f, -0.5f, -1.05f,
				0.5f, -0.5f, -1.05f,
				0.5f, 0.5f, -1.05f
		}, new float[] {
				1.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 1.0f
		}, new int[] {
				0, 1, 3, 3, 1, 2
		})));
	}
	
	public void onUpdate() {
		
	}
	
	public void onGameInit() {
		Cubulus.info("Initialized core game.");
		Cubulus.getInstance().getEventHandler().addListener(EventHandler.KEY_DOWN_EVENT, (data) -> onKeyDown(data.getLong("window"), data.getInt("key")));
		Cubulus.getInstance().getEventHandler().addListener(EventHandler.KEY_UP_EVENT, (data) -> onKeyUp(data.getLong("window"), data.getInt("key")));
	}
	
	public void onRender() {
		renderer.render(gameItems.toArray(new GameItem[gameItems.size()]));
	}
	
	public void onRenderInit() {
		renderer = new Renderer();
		renderer.init();
	}
	
	public void onCleanup() {
		Cubulus.info("Cleanup time.");
		for(GameItem item : gameItems) {
			item.getMesh().cleanUp();
		}
		renderer.cleanup();
	}
	
	public void onKeyDown(long window, int key) {
		if(key == GLFW.GLFW_KEY_ESCAPE) {
			Cubulus.getInstance().closeGame();
		}
	}
	
	public void onKeyUp(long window, int key) {
		
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