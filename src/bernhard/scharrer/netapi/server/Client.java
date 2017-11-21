package bernhard.scharrer.netapi.server;

import java.net.Socket;

import bernhard.scharrer.netapi.packet.Packet;

public class Client {
	
	private Channel channel;
	private Console console;
	private String ip;
	private TrafficManager manager;
	
	Client(TrafficManager manager, Socket socket, Console console) {
		
		this.manager = manager;
		this.ip = socket.getInetAddress().getHostAddress();
		this.channel = new Channel(this, manager, socket, console);
		this.console = console;
		
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

	public String getIP() {
		return ip;
	}
	
}
