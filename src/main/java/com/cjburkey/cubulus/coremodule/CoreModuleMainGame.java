package com.cjburkey.cubulus.coremodule;

import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.lwjgl.Renderer;

public final class CoreModuleMainGame extends ICoreModule {
	
	private Renderer renderer;
	
	public void onGlfwInit(boolean onRenderThread) {
		Cubulus.info("GLFW Init");
	}
	
	public void onRenderInit(boolean onRenderThread) {
		Cubulus.info("Render Init");
		
		renderer = new Renderer();
		try {
			renderer.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onGameInit(boolean onRenderThread) {
		Cubulus.info("Game Init");
	}
	
	public void onRenderUpdate(boolean onRenderThread) {
		renderer.render();
	}
	
	public void onGameTick(boolean onRenderThread) {
		
	}
	
	public void onRenderCleanup(boolean onRenderThread) {
		Cubulus.info("Render cleanup");
		
		renderer.cleanup();
	}
	
	public void onGameCleanup(boolean onRenderThread) {
		Cubulus.info("Game cleanup");
	}
	
}