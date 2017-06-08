package com.cjburkey.cubulus.coremodule;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.reflections.Reflections;
import com.cjburkey.cubulus.Cubulus;

public final class CoreModuleHandler {
	
	private final Queue<ICoreModule> loadedModules;
	private final EventSystem events;
	private final Reflections reflections;
	
	public CoreModuleHandler() {
		loadedModules = new ConcurrentLinkedQueue<ICoreModule>();
		events = new EventSystem();
		reflections = new Reflections("");
	}
	
	public void findAndLoadModules() {
		loadedModules.clear();
		Set<Class<? extends ICoreModule>> classes = reflections.getSubTypesOf(ICoreModule.class);
		for(Class<? extends ICoreModule> clazz : classes) {
			registerModule(clazz);
		}
	}
	
	private boolean registerModule(Class<? extends ICoreModule> moduleClass) {
		if(!isModuleLoaded(moduleClass)) {
			ICoreModule mod = instantiateClass(moduleClass);
			Cubulus.info("Loaded CoreModule: \"" + mod.getModuleName() + "\"");
			return loadedModules.add(mod);
		}
		return false;
	}
	
	private boolean isModuleLoaded(Class<? extends ICoreModule> moduleClass) {
		for(ICoreModule m : loadedModules) {
			if(m.equals(instantiateClass(moduleClass))) {
				return true;
			}
		}
		return false;
	}
	
	private ICoreModule instantiateClass(Class<? extends ICoreModule> moduleClass) {
		try {
			return moduleClass.newInstance();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public EventSystem getEventSystem() {
		return events;
	}
	
	public class EventSystem {
		public void glfwInit() {
			loadedModules.forEach(e -> e.onGlfwInit(Cubulus.onRenderThread()));
		}
		
		public void renderInit() {
			loadedModules.forEach(e -> e.onRenderInit(Cubulus.onRenderThread()));
		}
		
		public void gameInit() {
			loadedModules.forEach(e -> e.onGameInit(Cubulus.onRenderThread()));
		}
		
		public void renderUpdate() {
			loadedModules.forEach(e -> e.onRenderUpdate(Cubulus.onRenderThread()));
		}
		
		public void gameTick() {
			loadedModules.forEach(e -> e.onGameTick(Cubulus.onRenderThread()));
		}
		
		public void renderCleanup() {
			loadedModules.forEach(e -> e.onRenderCleanup(Cubulus.onRenderThread()));
		}
		
		public void gameCleanup() {
			loadedModules.forEach(e -> e.onGameCleanup(Cubulus.onRenderThread()));
		}
	}
	
}