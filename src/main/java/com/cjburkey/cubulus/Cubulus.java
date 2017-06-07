package com.cjburkey.cubulus;

import com.cjburkey.cubulus.module.ModuleHandler;

public final class Cubulus {
	
	private static Cubulus instance;
	
	private final Logger logger;
	private final ModuleHandler moduleHandler;
	
	public static void main(String[] args) {
		(instance = new Cubulus()).init();
	}
	
	private Cubulus() {
		logger = new Logger("[Cubulus] %s");
		moduleHandler = new ModuleHandler();
	}
	
	private void init() {
		moduleHandler.findAndLoadModules();
	}
	
	public static Logger getLogger() {
		return instance.logger;
	}
	
}