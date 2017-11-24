package bernhard.scharrer.netapi.server;

import java.net.Socket;

import bernhard.scharrer.netapi.packet.Packet;

public class Client {
	
	private Channel channel;
	private DatagramHandler datagrams;
	private Console console;
	private String ip;
	private TrafficManager manager;
	private int uuid;
	private static int count_uuid = 0;
	
	Client(TrafficManager manager, Socket socket, Console console, int buffer_length) {
		
		this.manager = manager;
		this.ip = socket.getInetAddress().getHostAddress();
		this.channel = new Channel(this, manager, socket, console);
		this.console = console;
		this.uuid = count_uuid++;
		
		if (buffer_length!=-1) {
			datagrams = new DatagramHandler(this, manager, console, ip, socket.getPort(), buffer_length);
		}
		
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
		datagrams.send(data);
	}
	
	public void send(float[] data) {
//		datagrams.send(data);
	}

	public String getIP() {
		return ip;
	}
	
	public int getUUID() {
		return uuid;
	}
	
}
