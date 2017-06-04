package com.cjburkey.cubulus.logic;

import java.util.Scanner;
import com.cjburkey.cubulus.Cubulus;

public class GameLogicCore implements IGameLogic {
	
	public boolean init() {
		Cubulus.getInstance().getLogger().info("Initialized core game.");
		asciiPrint();
		return true;
	}
	
	public boolean update() {
		return false;
	}
	
	public boolean render() {
		return false;
	}
	
	private void asciiPrint() {
		Cubulus.getInstance().getLogger().info("Printing some fun ascii text:");
		String file = "/ascii.txt";
		try {
			Scanner scanner = new Scanner(getClass().getResourceAsStream(file));
			while(scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
				Thread.sleep(250);
			}
			scanner.close();
		} catch(Exception e) {
			Cubulus.getInstance().error(-2, false, e);
		}
		Cubulus.getInstance().getLogger().info("Done.");
	}
	
}