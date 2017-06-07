package com.cjburkey.cubulus.module;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.reflections.Reflections;
import com.cjburkey.cubulus.Cubulus;

public final class ModuleHandler {
	
	private final Queue<IModule> loadedModules;
	private final EventSystem events;
	private final Reflections reflections;
	
	public ModuleHandler() {
		loadedModules = new ConcurrentLinkedQueue<IModule>();
		events = new EventSystem();
		reflections = new Reflections("");
	}
	
	public void findAndLoadModules() {
		loadedModules.clear();
		Set<Class<? extends IModule>> classes = reflections.getSubTypesOf(IModule.class);
		for(Class<? extends IModule> clazz : classes) {
			registerModule(clazz);
		}
	}
	
	private boolean registerModule(Class<? extends IModule> moduleClass) {
		if(!isModuleLoaded(moduleClass)) {
			IModule mod = instantiateClass(moduleClass);
			Cubulus.getLogger().info("Loaded game module: \"" + mod.getModuleName() + "\"");
			return loadedModules.add(mod);
		}
		return false;
	}
	
	private boolean isModuleLoaded(Class<? extends IModule> moduleClass) {
		for(IModule m : loadedModules) {
			if(m.equals(instantiateClass(moduleClass))) {
				return true;
			}
		}
		return false;
	}
	
	private IModule instantiateClass(Class<? extends IModule> moduleClass) {
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
		public void gameInit() {
			loadedModules.forEach(e -> e.onGameInit());
		}

		public void glfwInit() {
			loadedModules.forEach(e -> e.onGlfwInit());
		}
		
		public void renderInit() {
			loadedModules.forEach(e -> e.onRenderInit());
		}
		
		public void gameTick() {
			loadedModules.forEach(e -> e.onGameTick());
		}
		
		public void renderUpdate() {
			loadedModules.forEach(e -> e.onRenderUpdate());
		}
	}
	
}