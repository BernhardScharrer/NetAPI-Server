package networking;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channels.ByteChannel;
import networking.channels.Channel;
import networking.channels.ChannelType;
import networking.channels.MainChannel;
import networking.channels.PacketChannel;
import networking.channels.StreamManager;
import networking.channels.StringChannel;
import networking.channels.UDPReceiver;
import networking.channels.UDPSender;
import utils.Console;
import utils.ErrorType;
import utils.UUIDGenerator;

/**
 * 
 * represents a connection to the a client
 *
 */
public class Connection {

	private Server server;
	private Socket socket;
	
	private MainChannel main;
	private List<Channel> channels = new ArrayList<>();
	
	private ChannelType type = ChannelType.NONE;
	private String name = "-";
	private int size = -1;
	
	private int uuid;
	
	private UDPSender udp_sender;
	private UDPReceiver udp_receiver;
	
	private static int next_uuid = -1;

	public Connection(Server server, Socket socket) {
		
		this.uuid = UUIDGenerator.generate();
		
		this.server = server;
		this.socket = socket;
		
		main = new MainChannel();
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
		
		Channel channel = null;
		
		switch (type) {
		case BYTE:
			channel = new ByteChannel(name, size);
			break;
		case PACKET:
			channel = new PacketChannel(name);
			break;
		case STRING:
			channel = new StringChannel(name);
			break;
		case NONE:
			getConsole().warn("Trying to setup to connections on same IP!");
			return false;
		}
		
		type = ChannelType.NONE;
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
		if (udp_sender != null) udp_sender.close();
		if (udp_receiver != null) udp_receiver.close();
		if (main != null) main.stop();
		for (Channel channel : channels) channel.stop();
		if (socket!=null) {
			String ip = socket.getInetAddress().toString();
			try {
				socket.close();
				getConsole().debug("Closed Socket! ("+ip+")");
			} catch (IOException e) {
				getConsole().error("Could not close socket! ("+ip+")");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * methods
	 */

	public void recieveFromClient(String msg) {
		
		String[] args = msg.split(";");
		
		switch (args[0]) {
		
		case "channel":
			next_uuid = Integer.parseInt(args[1]);
			type = ChannelType.valueOf(args[2]);
			name = args[3];
			size = type == ChannelType.BYTE ? Integer.parseInt(args[4]) : -1;
		
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
	
	public Server getServer() {
		return server;
	}
	
	public Console getConsole() {
		return server.getConsole();
	}
	
	public String getIP() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public StreamManager getStreamManager() {
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
	
	public Channel getChannel(String channel_name) {
		for (Channel channel : channels) if (channel.getName().equals(channel_name)) return channel;
		return null;
	}
	
	/**
	 * enable udp sender
	 * @return 
	 */
	public UDPSender startUDPSender(int buffer_length) {
		udp_sender = new UDPSender(socket.getInetAddress().getHostAddress(), socket.getPort()+2, buffer_length, server.getConsole());
		sendToClient("udp;receive;"+(Server.udp_port++)+";"+buffer_length);
		return udp_sender;
	}
	
	/**
	 * enable udp receiver
	 * @return 
	 */
	public UDPReceiver startUDPReceiver(int buffer_length) {
		udp_receiver = new UDPReceiver(socket.getInetAddress().getHostAddress(), socket.getPort()+1, buffer_length, server.getConsole());
		sendToClient("udp;send;"+(Server.udp_port++)+";"+buffer_length);
		return udp_receiver;
	}
	
	public UDPReceiver getUDPReceiver() {
		return udp_receiver;
	}
	
	public UDPSender getUDPSender() {
		return udp_sender;
	}
	
}
