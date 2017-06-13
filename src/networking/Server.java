package networking;

import java.net.Socket;

import networking.utils.Console;

public class Server {
	
	private String ip;
	private int port;
	private Console console;
	private SocketListener listener;
	
	/**
	 * 
	 * represents the server
	 * 
	 */
	public Server(String ip, int port, Console console) {
		
		this.ip = ip;
		this.port = port;
		this.console = console;
		this.listener = new SocketListener(this, console);
		
	}
	
	/**
	 * incoming socket from listener
	 */
	public void newSocket(Socket socket) {
		
		// TODO
		
	}

	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
}
