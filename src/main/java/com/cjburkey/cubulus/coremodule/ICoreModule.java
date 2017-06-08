package com.cjburkey.cubulus.coremodule;

public abstract class ICoreModule {
	
	public String getModuleName() {
		return getClass().getSimpleName();
	}

	public abstract void onGlfwInit(boolean onRenderThread);
	public abstract void onRenderInit(boolean onRenderThread);
	public abstract void onGameInit(boolean onRenderThread);
	public abstract void onRenderUpdate(boolean onRenderThread);
	public abstract void onGameTick(boolean onRenderThread);
	public abstract void onRenderCleanup(boolean onRenderThread);
	public abstract void onGameCleanup(boolean onRenderThread);
	
	public boolean equals(Object other) {
		return ((other != null) && (other instanceof ICoreModule) && (other.getClass().equals(getClass())));
	}
	
}