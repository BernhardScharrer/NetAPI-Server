package udp.networking;

import utils.Console;

public class UDPServer {
	
	private String ip;
	private int port;
	private Console console;
	private int buffer_length;
	private UDPSocketListener listener;
	
	public UDPServer(String ip, int port, Console console, int buffer_length) {
		this.ip = ip;
		this.port = port;
		this.console = console;
		this.buffer_length = buffer_length;
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
	
}
