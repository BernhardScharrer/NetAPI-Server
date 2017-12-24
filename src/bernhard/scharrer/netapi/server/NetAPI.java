package bernhard.scharrer.netapi.server;

public class NetAPI {
	
	private static Server server;
	
	/**
	 * @param ip
	 * @param port
	 * @param traffic
	 * @param console
	 * @return
	 */
	public static Server start(String ip, int port, TrafficManager traffic, Console console) {
		return server = new Server(ip, port, -1, -1, traffic, console);
	}
	
	/**
	 * @param ip
	 * @param port
	 * @param uport
	 * @param buffer
	 * @param traffic
	 * @param console
	 * @return
	 */
	public static Server start(String ip, int port, int uport, int buffer, TrafficManager traffic, Console console) {
		return server = new Server(ip, port, uport, buffer, traffic, console);
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
