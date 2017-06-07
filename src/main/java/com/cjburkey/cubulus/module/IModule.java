package com.cjburkey.cubulus.module;

public abstract class IModule {
	
	public String getModuleName() {
		return getClass().getSimpleName();
	}

	public abstract void onGameInit();
	public abstract void onGlfwInit();
	public abstract void onRenderInit();
	public abstract void onGameTick();
	public abstract void onRenderUpdate();
	
	public boolean equals(Object other) {
		return ((other != null) && (other instanceof IModule) && (other.getClass().equals(getClass())));
	}
	
}