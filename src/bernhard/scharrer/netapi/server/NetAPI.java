package bernhard.scharrer.netapi.server;

public class NetAPI {
	
	private static final int NO_UDP = -1;
	private static Server server;
	
	/**
	 * <p>
	 * Simple way to create a server. In this mode only String and Packets
	 * can be transfered (only TCP). The console output will be in linux style.
	 * <p>
	 * @param ip to bind socket listener to
	 * @param port to bind to
	 * @param traffic manager for incoming stuff
	 */
	public static Server start(String ip, int port, TrafficManager traffic) {
		return start(true, new LinuxConsole(false), ip, port, NO_UDP, traffic);
	}
	/**
	 * <p>
	 * In this mode only String and Packets can be transfered (only TCP).
	 * You can create your own console output in this mode.
	 * <p>
	 * You also can disable the headline to be printed and
	 * you are allowed to enable debug messages.
	 * <p>
	 * @param headline should be printed?
	 * @param console which should be used
	 * @param ip to bind socket listener to
	 * @param port to bind to
	 * @param traffic manager for incoming stuff
	 */
	public static Server start(boolean headline, Console console, String ip, int port, TrafficManager traffic) {
		return start(headline, console, ip, port, NO_UDP, traffic);
	}
	
	/**
	 * <p>
	 * In this mode you can transfer Strings, Packets, int[]s and float[]s.
	 * The console output will be in linux style.
	 * <p>
	 * @param ip to bind socket listener to
	 * @param port to bind to
	 * @param buffer_length represents the size of the arrays that can be transfered via datagrams
	 * @param traffic manager for incoming stuff
	 */
	public static Server start(String ip, int port, int buffer_length, TrafficManager traffic) {
		return start(true, new LinuxConsole(false), ip, port, buffer_length, traffic);
	}
	
	/**
	 * <p>
	 * In this mode you can transfer Strings, Packets, int[]s and float[]s.
	 * You can create your own console output in this mode.
	 * <p>
	 * You also can disable the headline to be printed and
	 * you are allowed to enable debug messages.
	 * <p>
	 * @param ip to bind socket listener to
	 * @param port to bind to
	 * @param buffer_length represents the size of the arrays that can be transfered via datagrams
	 * @param traffic manager for incoming stuff
	 */
	public static Server start(boolean headline, Console console, String ip, int port, int buffer_length, TrafficManager traffic) {
		if (headline) printHeadline();
		return server = new Server(ip, port, buffer_length, traffic, console);
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
