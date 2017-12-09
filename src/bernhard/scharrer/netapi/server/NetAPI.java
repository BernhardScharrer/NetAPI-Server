package bernhard.scharrer.netapi.server;

public class NetAPI {
	
	private static Server server;
	
	public static Server start(String ip, int port, TrafficManager tcp_modul, Console console) {
		return server = new Server(ip, port, -1, -1, tcp_modul, console);
	}
	
	public static Server start(String ip, int port, int uport, int buffer, TrafficManager tcp_modul, Console console) {
		return server = new Server(ip, port, uport, buffer, tcp_modul, console);
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
	
}
