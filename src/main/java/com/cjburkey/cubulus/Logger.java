package com.cjburkey.cubulus;

import java.io.PrintStream;

public final class Logger {
	
	private final String format;
	
	public Logger(String format) {
		this.format = format;
	}
	
	public Logger() {
		this("[Cubulus] %s");
	}
	
	public void log(PrintStream stream, Object msg) {
		String out = (msg == null) ? "null" : msg.toString();
		stream.println(String.format(format, out));
	}
	
	public void info(Object msg) {
		log(System.out, msg);
	}
	
	public void err(Object msg) {
		log(System.err, msg);
	}
	
	public String getFormat() {
		return format;
	}
	
}