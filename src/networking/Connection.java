package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channels.Channel;
import networking.channels.ChannelType;
import networking.channels.MainChannel;
import networking.channels.StringChannel;
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
	private List<Channel> channels = new ArrayList<>();
	
	private ChannelType type;
	private String name;
	private int size;

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
	
	public void addChannel(Socket socket) {
		
		Channel channel = null;
		
		switch (type) {
		case BYTE:
			break;
		case PACKET:
			break;
		case STRING:
			channel = new StringChannel(name);
			break;
		case NONE:
			getConsole().error("No channel data in buffers!");
			break;
		}
		
		type = ChannelType.NONE;
		name = "-";
		size = -1;
		
		if (channel != null) {
			channel.init(socket, this, getConsole());
			channel.start();
			channels.add(channel);
		}
		
		
	}
	
	public void close() {
		main.stop();
		for (Channel channel : channels) channel.stop();
	}
	
	/**
	 * methods
	 */

	public void recieveFromClient(String msg) {
		
		String[] args = msg.split(";");
		
		switch (args[0]) {
		
		case "channel":
			type = ChannelType.valueOf(args[1]);
			name = args[2];
			size = type == ChannelType.BYTE ? Integer.parseInt(args[3]) : -1;
		
			sendToClient("channel;accept");
			
		}
		
	}
	
	private void sendToClient(String msg) {
		main.send(msg);
	}

	public void disconnect() {
		server.disconnect(this);
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
	
	public StreamManager getStreamManager() {
		return server.getStreamManager();
	}
	
}
