package com.cjburkey.cubulus.core;

import com.cjburkey.cubulus.Cubulus;

public final class GameLoop {
	
	private final boolean outputUps;
	
	private Thread thread;
	private boolean running = false;
	private int ups;
	
	public GameLoop(String name, int ups, Runnable update, boolean displayUps) {
		outputUps = displayUps;
		thread = new Thread(new Call(ups, update));
		thread.setName(name);
	}
	
	public void start() {
		running = true;
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	public int getUpdatesPerSecond() {
		return ups;
	}
	
	private class Call implements Runnable {
		private double step = 0.0d;
		private Runnable update;
		public Call(int ups, Runnable update) {
			step = 1000000000d / (double) ups;
			this.update = update;
		}
		
		private boolean first = true;
		public void run() {
			long lastUpdate = System.nanoTime();
			long lastSecond = System.currentTimeMillis();
			int updates = 0;
			while(running) {
				if(first) {
					first = false;
					Cubulus.getCoreEventDispatcherForLogicEvents().gameInit();
				}
				long nowUpdate = System.nanoTime();
				long nowSecond = System.currentTimeMillis();
				if(nowUpdate - lastUpdate >= step) {
					lastUpdate = nowUpdate;
					updates ++;
					update.run();
				}
				if(nowSecond - lastSecond >= 1000) {
					lastSecond = nowSecond;
					ups = updates;
					updates = 0;
					if(outputUps) {
						Cubulus.info("UPS: " + ups);
					}
				}
			}
			Cubulus.getCoreEventDispatcherForLogicEvents().gameCleanup();
		}
	}
	
}