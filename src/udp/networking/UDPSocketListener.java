package udp.networking;

import java.net.DatagramSocket;
import java.net.SocketException;

import utils.Console;

public class UDPSocketListener {

	private Thread listener;
	private DatagramSocket socket;
	
	/**
	 * listens on a udp server for new connections
	 * 
	 * @param server
	 */
	public UDPSocketListener(UDPServer server) {
		
		Console console = server.getConsole();
		
		listener = new Thread(() -> {
			
			console.debug("UDP socket listener started!");
			try {
				
				while (true) {
				
					socket = new DatagramSocket(server.getPort());
					server.newConnection(socket);
					
				}
				
			} catch (SocketException e) {
				server.getManager().bindFail(socket);
			} finally {
				server.disconnect();
			}
			
		});
		
		listener.start();
		
	}

	/**
	 * stop listener
	 */
	public void disconnect() {
		if (listener != null) listener.interrupt();
	}
	
}
