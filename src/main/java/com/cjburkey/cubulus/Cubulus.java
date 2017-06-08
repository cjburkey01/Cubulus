package com.cjburkey.cubulus;

import com.cjburkey.cubulus.coremodule.CoreModuleHandler;

public final class Cubulus {
	
	private static Cubulus instance;
	
	private final Logger logger;
	private final CoreModuleHandler moduleHandler;
	
	public static void main(String[] args) {
		(instance = new Cubulus()).init();
	}
	
	private Cubulus() {
		logger = new Logger("[Cubulus-%s] %s");
		moduleHandler = new CoreModuleHandler();
	}
	
	private void init() {
		moduleHandler.findAndLoadModules();
	}
	
	public static Cubulus getInstance() {
		return instance;
	}
	
	public static Logger getLogger() {
		return instance.logger;
	}
	
}