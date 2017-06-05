package com.cjburkey.cubulus.logic;

import org.lwjgl.glfw.GLFW;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.event.EventHandler;

public final class GameLogicCore implements IGameLogic {
	
	public void onUpdate() {
		
	}
	
	public void onGameInit() {
		Cubulus.info("Initialized core game.");
		//asciiPrint();
		Cubulus.getInstance().getEventHandler().addListener(EventHandler.KEY_DOWN_EVENT, (data) -> onKeyDown(data.getLong("window"), data.getInt("key")));
		Cubulus.getInstance().getEventHandler().addListener(EventHandler.KEY_UP_EVENT, (data) -> onKeyUp(data.getLong("window"), data.getInt("key")));
	}
	
	public void onRender() {
		
	}
	
	public void onRenderInit() {
		
	}
	
	public void onCleanup() {
		Cubulus.info("Cleanup time.");
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