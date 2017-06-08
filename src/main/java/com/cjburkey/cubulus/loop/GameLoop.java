package com.cjburkey.cubulus.loop;

public class GameLoop {
	
	public static final int TICKS_PER_SECOND = 60;
	public static final int NANOS_PER_SECOND = 1000000000;
	public static final double BETWEEN_TICKS = ((double) TICKS_PER_SECOND / (double) NANOS_PER_SECOND);
	
	private boolean running = false;
	private boolean firstTick = true;
	private long lastTick = System.nanoTime();
	private final Thread thread;
	private final Runnable firstTickCall;
	private final Runnable tick;
	private final Runnable cleanup;
	
	public GameLoop(Runnable firstTick, Runnable tick, Runnable cleanup) {
		thread = new Thread(() -> run());
		thread.setName("logicloop");
		firstTickCall = firstTick;
		this.tick = tick;
		this.cleanup = cleanup;
	}
	
	public void start() {
		thread.start();
	}
	
	public void stop() {
		running = false;
	}
	
	private void run() {
		running = true;
		while(running) {
			long now = System.nanoTime();
			if(now - lastTick >= BETWEEN_TICKS) {
				lastTick = now;
				tick();
			}
		}
		cleanup.run();
	}
	
	private void tick() {
		if(firstTick) {
			firstTick = false;
			firstTickCall.run();
		}
		tick.run();
	}
	
}