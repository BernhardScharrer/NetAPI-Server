package udp.networking;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import utils.Console;

public class UDPServer {
	
	private String ip;
	private int port;
	private Console console;
	private int buffer_length;
	private UDPSocketListener listener;
	private UDPStreamManager manager;
	
	private List<UDPConnection> cons = new ArrayList<>();
	
	/**
	 * 
	 * constructs a udp server
	 * 
	 * @param ip
	 * @param port
	 * @param console
	 * @param buffer_length
	 * @param manager
	 */
	public UDPServer(String ip, int port, Console console, int buffer_length, UDPStreamManager manager) {
		
		this.console.info("UDP server is going to start on port: " + port);
		
		this.ip = ip;
		this.port = port;
		this.console = console;
		this.buffer_length = buffer_length;
		this.manager = manager;
		this.listener = new UDPSocketListener(this);
		
	}

	public String getIp() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}
	
	public Console getConsole() {
		return console;
	}

	public int getBufferLength() {
		return buffer_length;
	}

	public UDPStreamManager getManager() {
		return manager;
	}
	
	/**
	 * disconnect all udp connections
	 */
	public void disconnect() {
		listener.disconnect();
		for (UDPConnection con : cons) con.disconnect();
	}
	
	/**
	 * set up new udp connection from socket
	 * 
	 * @param socket
	 */
	public void newConnection(DatagramSocket socket) {
		cons.add(new UDPConnection(this, socket, buffer_length));
	}
	
	/**
	 * get connection via uuid
	 * 
	 * @param uuid of connection
	 * @return connection if found
	 */
	public UDPConnection getConnection(int uuid) {
		for (UDPConnection con : cons) if (con.getUUID() == uuid) return con;
		return null;
	}
	
}
