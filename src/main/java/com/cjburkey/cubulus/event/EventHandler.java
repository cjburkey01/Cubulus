package com.cjburkey.cubulus.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EventHandler {
	
	public static final String KEY_DOWN_EVENT = "KEY_DOWN_EVENT";	// Called when key is pressed.
	public static final String KEY_UP_EVENT = "KEY_UP_EVENT";		// Called when key is released.
	
	private HashMap<String, List<EventListener>> events = new HashMap<>();
	
	public void publishEvent(String eventName, EventData data) {
		List<EventListener> listeners = events.get(eventName);
		if(listeners != null) {
			for(EventListener el : listeners) {
				el.eventFired(data);
			}
		}
	}
	
	public void addListener(String eventName, EventListener listener) {
		List<EventListener> list = events.get(eventName);
		if(list == null) {
			list = new ArrayList<>();
		}
		list.add(listener);
		events.put(eventName, list);
	}
	
	public void clearEvent(String eventName) {
		events.put(eventName, new ArrayList<EventListener>());
	}
	
}