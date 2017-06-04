package com.cjburkey.cubulus;

public final class Info {
	
	public static final int GAME_MAJOR = 0;
	public static final int GAME_MINOR = 0;
	public static final int GAME_PATCH = 1;
	
	public static String getGameVersion() {
		StringBuilder b = new StringBuilder();
		b.append(GAME_MAJOR);
		b.append('.');
		b.append(GAME_MINOR);
		b.append('.');
		b.append(GAME_PATCH);
		return b.toString();
	}
	
}