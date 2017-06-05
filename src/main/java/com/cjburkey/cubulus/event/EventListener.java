package com.cjburkey.cubulus.event;

@FunctionalInterface
public interface EventListener {
	
	void eventFired(EventData data);
	
}