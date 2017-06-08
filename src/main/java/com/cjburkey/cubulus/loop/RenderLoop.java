package com.cjburkey.cubulus.loop;

public class RenderLoop {
	
	private boolean running = false;
	private boolean firstTick = true;
	
	private final Runnable firstTickCall;
	private final Runnable update;
	private final Runnable cleanup;
	
	public RenderLoop(Runnable first, Runnable update, Runnable cleanup) {
		firstTickCall = first;
		this.update = update;
		this.cleanup = cleanup;
	}
	
	public void start() {
		running = true;
		while(running) {
			tick();
		}
		cleanup.run();
	}
	
	public void stop() {
		running = false;
	}
	
	private void tick() {
		if(firstTick) {
			firstTick = false;
			firstTickCall.run();
		}
		update.run();
	}
	
}