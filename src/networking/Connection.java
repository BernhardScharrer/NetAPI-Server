package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channel.Channel;
import networking.channel.ChannelType;
import networking.channel.MainChannel;

public class Connection {
	
	private Server server;
	
	private String ip;
	private MainChannel main_channel;
	private List<Channel> channels = new ArrayList<>();
	private ChannelType type;
	private String channel_name;
	private Console console;
	
	public Connection(Server server, Socket socket, Console console) {
		
		console.debug("Build new connection on " + socket.getInetAddress().getHostAddress());
		this.server = server;
		this.console = console;
		this.ip = socket.getInetAddress().getHostAddress();
		this.main_channel = new MainChannel(socket, this, console);
		this.type = ChannelType.NONE;
		this.channel_name = "-";
		
	}

	public String getIP() {
		return ip;
	}

	public void close() {
		main_channel.close();
		for (Channel channel : channels) channel.stop();
	}

	public ChannelType getState() {
		return type;
	}
	
	public String getNextChannelName() {
		return channel_name;
	}
	
	public void addChannel(Channel channel) {
		this.channels.add(channel);
	}
	
	public void send(String msg) {
		main_channel.send(msg);
	}
	
	public void incoming(String command) {
		
		console.debug("Incoming command on MAIN-Channel: " + command);		
		String[] args = command.split(";");
		
		switch (args[0]) {
		case "disconnect":
			server.disconnect(this);
			close();
			break;
		case "channel":
			type = ChannelType.valueOf(args[1].toUpperCase());
			channel_name = args[2];
			console.debug("Channeltype was set to: " + args[1].toUpperCase());
			break;
		}
	}
	
}
