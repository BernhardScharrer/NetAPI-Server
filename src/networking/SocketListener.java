package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ServerSocketFactory;

import networking.utils.Console;

public class SocketListener {
	
	private Thread listener;
	private ServerSocket socket;
	private Console console;
	
	/**
	 * 
	 * socket listener listens when new sockets come in
	 * and links them with the right connection or if none
	 * exists creates a new one
	 * 
	 */
	public SocketListener(Server server, Console console) {
		
		console.info("Listener is going to start now...");
		
		this.console = console;
		
		listener = new Thread(new Runnable() {

			public void run() {
				
				/**
				 * creates a new socket listener (also a socket)
				 * which handels sockets which are incoming from clients.
				 */
				try {
					
					Socket new_socket;
					socket = ServerSocketFactory.getDefault().createServerSocket(server.getPort());
					
					console.debug("Succesfully set up socket listener!");
					
					while (true) {
						
						/**
						 * waits for incoming socket
						 */
						
						console.debug("Incoming socket from: " + socket.getInetAddress().getHostAddress());
						
						new_socket = socket.accept();
						server.newSocket(new_socket);
						
					}
					
				} catch (UnknownHostException e) {
					console.error("Unknown host!");
					e.printStackTrace();
				} catch (IOException e) {
					console.error("Could not create listener on: " + server.getIP() + ":" + server.getPort());
					e.printStackTrace();
				}
				
				
				
			}
		});
		
		listener.start();
		
	}
	
	/**
	 * stops the listener
	 */
	public void stop() {
		listener.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			console.error("Could not stop server! (Socket cant close)");
			e.printStackTrace();
		}
	}
	
}
