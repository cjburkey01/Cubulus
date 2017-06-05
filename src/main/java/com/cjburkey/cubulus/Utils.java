package com.cjburkey.cubulus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public final class Utils {
	
	public static String loadResource(String path) throws Exception {
		StringBuilder out = new StringBuilder();
		Scanner scanner = new Scanner(Utils.class.getResourceAsStream(path));
		while(scanner.hasNextLine()) {
			out.append(scanner.nextLine() + '\n');
		}
		scanner.close();
		return out.toString();
	}
	
	public static List<String> loadResourceLines(String path) throws Exception {
		List<String> out = new ArrayList<>();
		Scanner scanner = new Scanner(Utils.class.getResourceAsStream(path));
		while(scanner.hasNextLine()) {
			out.add(scanner.nextLine() + '\n');
		}
		scanner.close();
		return out;
	}
	
	public static int randomRangei(int min, int max, boolean inclusive) {
		return ThreadLocalRandom.current().nextInt(min, max + ((inclusive) ? 1 : 0));
	}
	
	public static float randomRangef(float min, float max, boolean inclusive) {
		return (ThreadLocalRandom.current().nextFloat() * (max + ((inclusive) ? 1 : 0) - min)) + min;
	}
	
	public static float clamp(float value, float min, float max) {
		if(value <= min) return min;
		if(value >= max) return max;
		return value;
	}
	
	public static float wrap(float value, float min, float max) {
		if(value < min) return max;
		if(value > max) return min;
		return value;
	}
	
}