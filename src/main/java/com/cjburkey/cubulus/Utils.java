package com.cjburkey.cubulus;

import java.util.Scanner;

public class Utils {
	
	public static String loadResource(String path) throws Exception {
		StringBuilder out = new StringBuilder();
		Scanner scanner = new Scanner(Utils.class.getResourceAsStream(path));
		while(scanner.hasNextLine()) {
			out.append(scanner.nextLine());
			out.append('\n');
		}
		scanner.close();
		return out.toString();
	}
	
}