package com.cjburkey.cubulus.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.resource.ResourceHandler;

public class AsciiMessage {
	
	public AsciiMessage() {
		Cubulus.info("Printing some fun ascii text:");
		try {
			Scanner scanner = new Scanner(ResourceHandler.getInstance().getStream("cubulus:fun/consoleAsciiPrint.msg"));
			List<String> output = new ArrayList<>();
			while(scanner.hasNextLine()) {
				output.add(scanner.nextLine());
			}
			scanner.close();
			for(String s : output) {
				System.out.println("\t" + s);
			}
		} catch(Exception e) {
			Cubulus.getInstance().error(-2, false, e);
		}
	}
	
}