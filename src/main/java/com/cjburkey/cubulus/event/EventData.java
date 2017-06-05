package com.cjburkey.cubulus.event;

import java.util.HashMap;

public class EventData {
	
	private HashMap<String, Object> data;
	
	public EventData() {
		data = new HashMap<>();
	}
	
	public void set(String key, Object value) {
		data.put(key, value);
	}
	
	public String getString(String key) {
		if(data.get(key) instanceof String) {
			return (String) data.get(key);
		}
		return null;
	}
	
	public int getInt(String key) {
		if(data.get(key) instanceof Integer) {
			return (Integer) data.get(key);
		}
		return Integer.MAX_VALUE;
	}
	
	public long getLong(String key) {
		if(data.get(key) instanceof Long) {
			return (Long) data.get(key);
		}
		return Long.MAX_VALUE;
	}
	
}