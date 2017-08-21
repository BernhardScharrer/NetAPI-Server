package tcp.networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import tcp.networking.channels.TCPByteChannel;
import tcp.networking.channels.TCPChannel;
import tcp.networking.channels.TCPChannelType;
import tcp.networking.channels.TCPMainChannel;
import tcp.networking.channels.TCPPacketChannel;
import tcp.networking.channels.TCPStringChannel;
import utils.Console;
import utils.ErrorType;
import utils.UUIDGenerator;

/**
 * 
 * represents a connection to the a client
 *
 */
public class TCPConnection {

	private TCPServer server;
	private Socket socket;
	
	private TCPMainChannel main;
	private List<TCPChannel> channels = new ArrayList<>();
	
	private TCPChannelType type = TCPChannelType.NONE;
	private String name = "-";
	private int size = -1;
	
	private int uuid;
	
	private static int next_uuid = -1;

	public TCPConnection(TCPServer server, Socket socket) {
		
		this.uuid = UUIDGenerator.generate();
		
		this.server = server;
		this.socket = socket;
		
		main = new TCPMainChannel();
		main.init(socket, this, server.getConsole());
		main.start();
		main.waitLoading();
		
		main.send("uuid;"+uuid);
		
		server.getStreamManager().connect(this);
		
	}
	
	/**
	 * methods
	 */
	
	public boolean addChannel(Socket socket) {
		
		TCPChannel channel = null;
		
		switch (type) {
		case BYTE:
			channel = new TCPByteChannel(name, size);
			break;
		case PACKET:
			channel = new TCPPacketChannel(name);
			break;
		case STRING:
			channel = new TCPStringChannel(name);
			break;
		case NONE:
			getConsole().warn("Trying to setup to connections on same IP!");
			return false;
		}
		
		type = TCPChannelType.NONE;
		name = "-";
		size = -1;
		
		if (channel != null) {
			channel.init(socket, this, getConsole());
			channel.start();
			channels.add(channel);
		}
		
		return true;
		
	}
	
	public void close() {
		main.stop();
		for (TCPChannel channel : channels) channel.stop();
	}
	
	/**
	 * methods
	 */

	public void recieveFromClient(String msg) {
		
		String[] args = msg.split(";");
		
		switch (args[0]) {
		
		case "channel":
			next_uuid = Integer.parseInt(args[1]);
			type = TCPChannelType.valueOf(args[2]);
			name = args[3];
			size = type == TCPChannelType.BYTE ? Integer.parseInt(args[4]) : -1;
		
			sendToClient("channel;accept");
			
		}
		
	}
	
	private void sendToClient(String msg) {
		main.send(msg);
	}

	public void disconnect(ErrorType error) {
		server.disconnect(this, error);
	}
	
	/**
	 * getters
	 */
	
	public TCPServer getServer() {
		return server;
	}
	
	public Console getConsole() {
		return server.getConsole();
	}
	
	public String getIP() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public TCPStreamManager getStreamManager() {
		return server.getStreamManager();
	}
	
	public int getUUID() {
		return uuid;
	}
	
	public static int getNextUUID() {
		return next_uuid;
	}

	public static void clearUUID() {
		next_uuid = -1;
	}
	
	public TCPChannel getChannel(String channel_name) {
		for (TCPChannel channel : channels) if (channel.getName().equals(channel_name)) return channel;
		return null;
	}
	
}
