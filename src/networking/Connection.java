package networking;

import java.net.Socket;

import networking.channels.MainChannel;
import networking.utils.Console;

/**
 * 
 * represents a connection to the a client
 *
 */
public class Connection {

	private Server server;
	private Socket socket;
	
	private MainChannel main;

	public Connection(Server server, Socket socket) {
		
		this.server = server;
		this.socket = socket;
		
		main = new MainChannel();
		main.init(socket, this, server.getConsole());
		main.start();
		
	}
	
	/**
	 * methods
	 */
	
	public void addChannel() {
		
	}
	
	public void close() {
		main.stop();
	}
	
	/**
	 * methods
	 */

	public void recieveFromServer(String msg) {
		
	}
	
	public void send(String msg) {
		main.send(msg);
	}
	
	/**
	 * getters
	 */
	
	public Console getConsole() {
		return server.getConsole();
	}
	
	public String getIP() {
		return socket.getInetAddress().getHostAddress();
	}

	public void disconnect() {
		server.disconnect(this);
	}
	
}
