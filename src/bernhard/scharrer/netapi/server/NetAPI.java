package bernhard.scharrer.netapi.server;

public class NetAPI {
	
	private static Server server;
	
	public static Server start(boolean headline, Console console, String ip, int port, TCPModul tcpmodul) {
		if (headline) printHeadline();
		return server = new Server(ip, port, tcpmodul, console);
	}
	
	/**
	 * <p>
	 * Closes all open connection and stops
	 * the socket listener.
	 * <p>
	 */
	public static void stop() {
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
