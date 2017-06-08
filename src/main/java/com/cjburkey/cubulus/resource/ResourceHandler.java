package com.cjburkey.cubulus.resource;

import java.io.InputStream;

public final class ResourceHandler {
	
	public static InputStream getStream(String location) {
		if(validLocation(location)) {
			return ResourceHandler.class.getResourceAsStream(getPath(location));
		}
		return null;
	}
	
	public static boolean validLocation(String location) {
		return getPath(location) != null;
	}
	
	public static String getPath(String location) {
		String[] split = location.split(":");
		if(split.length == 2) {
			return "/" + split[0] + "/" + split[1];
		}
		return null;
	}
	
}