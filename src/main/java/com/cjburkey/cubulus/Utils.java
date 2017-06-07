package com.cjburkey.cubulus;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.Vector3f;
import com.cjburkey.cubulus.resource.ResourceHandler;

public final class Utils {
	
	public static String loadResourceAsString(String loc) throws Exception {
		StringBuilder out = new StringBuilder();
		Scanner scanner = new Scanner(ResourceHandler.getStream(loc));
		while(scanner.hasNextLine()) {
			out.append(scanner.nextLine() + '\n');
		}
		scanner.close();
		return out.toString();
	}
	
	public static List<String> loadResourceAsLines(String loc) throws Exception {
		List<String> out = new ArrayList<>();
		Scanner scanner = new Scanner(ResourceHandler.getStream(loc));
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
	
	public static float distance(Vector3f from, Vector3f to) {
		return from.distance(to);
	}
	
	public static float distance(Vector3f from, float toX, float toY, float toZ) {
		return from.distance(toX, toY, toZ);
	}
	
	public static float distance(float fromX, float fromY, float fromZ, float toX, float toY, float toZ) {
		return new Vector3f(fromX, fromY, fromZ).distance(toX, toY, toZ);
	}
	
}