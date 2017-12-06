package bernhard.scharrer.netapi.server;

import java.net.Socket;

import bernhard.scharrer.netapi.packet.Packet;

public class Client {
	
	private Channel channel;
	private Console console;
	private String ip;
	private TCPModul manager;
	private int uuid;
	private static int count_uuid = 0;
	
	Client(TCPModul manager, Socket socket, Console console, int buffer_length) {
		
		this.manager = manager;
		this.ip = socket.getInetAddress().getHostAddress();
		this.channel = new Channel(this, manager, socket, console);
		this.console = console;
		this.uuid = count_uuid++;
		
		manager.connect(this);
		
	}
	
	void cleanUp() {
		manager.disconnect(this);
		console.debug("Cleaning up client.");
		channel.cleanUp();
	}
	
	public void send(String message) {
		channel.send(message);
	}
	
	public void send(Packet packet) {
		channel.send(packet);
	}
	
	public void send(int[] data) {
		// TODO
	}
	
	public void send(float[] data) {
		// TODO
	}

	public String getIP() {
		return ip;
	}
	
	public int getUUID() {
		return uuid;
	}
	
}
