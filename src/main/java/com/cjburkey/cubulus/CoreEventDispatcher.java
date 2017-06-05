package com.cjburkey.cubulus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import com.cjburkey.cubulus.logic.IGameLogic;

public class CoreEventDispatcher {
	
	private final List<IGameLogic> logic;
	
	public CoreEventDispatcher() {
		logic = new ArrayList<>();
	}
	
	public void findLogic() throws Exception {
		Reflections reflections = new Reflections(Cubulus.class.getPackage().getName());
		Set<Class<? extends IGameLogic>> modules = reflections.getSubTypesOf(IGameLogic.class);
		for(Class<? extends IGameLogic> logic : modules) {
			IGameLogic instance = logic.newInstance();
			this.logic.add(instance);
			Cubulus.info("  - Found: " + logic.getSimpleName());
		}
	}
	
	public void gameInit() {
		Cubulus.info("Game init");
		for(IGameLogic engine : logic) {
			engine.onGameInit();
		}
	}
	
	public void renderInit() {
		Cubulus.info("Render init");
		for(IGameLogic engine : logic) {
			engine.onRenderInit();
		}
	}
	
	public void gameUpdate() {
		for(IGameLogic engine : logic) {
			engine.onGameUpdate();
		}
	}
	
	public void renderUpdate() {
		for(IGameLogic engine : logic) {
			engine.onRenderUpdate();
		}
	}
	
	public void gameCleanup() {
		Cubulus.info("Game cleanup");
		for(IGameLogic engine : logic) {
			engine.onGameCleanup();
		}
	}
	
	public void renderCleanup() {
		Cubulus.info("Render cleanup");
		for(IGameLogic engine : logic) {
			engine.onRenderCleanup();
		}
	}
	
}