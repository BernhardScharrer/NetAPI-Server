package bernhard.scharrer.netapi.server;

public class NetAPI {
	
	private static Server server;
	
	public static void start(String ip, int port, TrafficManager traffic) {
		printHeadline();
		server = new Server(ip, port, traffic, new LinuxConsole(false));
	}
	
	public static void start(boolean headline, boolean debug, String ip, int port, TrafficManager traffic) {
		if (headline) printHeadline();
		server = new Server(ip, port, traffic, new LinuxConsole(debug));
	}
	
	public static void start(boolean headline, Console console, String ip, int port, TrafficManager traffic) {
		if (headline) printHeadline();
		server = new Server(ip, port, traffic, console);
	}
	
	public void stop() {
		server.cleanUp();
	}
	
	private static void printHeadline() {
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
