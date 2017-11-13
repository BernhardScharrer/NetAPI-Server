package bernhard.scharrer.netapi;

import bernhard.scharrer.netapi.server.Console;

public class NetAPI {
	
	public NetAPI() {
		printHeadline();
	}
	
	public NetAPI(boolean headline, boolean debug, Console console) {
		if (headline) printHeadline();
	}
	
	public void stop() {
		
	}
	
	private void printHeadline() {
		System.out.println("\n ____  _____        _        _       _______  _____  ");
		System.out.println("|_   \\|_   _|      / |_     / \\     |_   __ \\|_   _| ");
		System.out.println("  |   \\ | |  .---.`| |-'   / _ \\      | |__) | | |   ");
		System.out.println("  | |\\ \\| | / /__\\\\| |    / ___ \\     |  ___/  | |   ");
		System.out.println(" _| |_\\   |_| \\__.,| |, _/ /   \\ \\_  _| |_    _| |_  ");
		System.out.println("|_____|\\____|'.__.'\\__/|____| |____||_____|  |_____| ");
		System.out.println("");
		System.out.println("Written by Bernhard Scharrer\n");
	}
	
}
