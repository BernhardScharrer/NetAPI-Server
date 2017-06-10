package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channel.Channel;
import networking.channel.ChannelType;
import networking.channel.MainChannel;

public class Connection {
	
	private String ip;
	private MainChannel main_channel;
	private List<Channel> channels = new ArrayList<>();
	private ChannelType type;
	
	public Connection(Socket socket, Console console) {
		
		console.debug("Build new connection on " + socket.getInetAddress().getHostAddress());
		this.ip = socket.getInetAddress().getHostAddress();
		this.main_channel = new MainChannel(socket, this, console);
		this.type = ChannelType.NONE;
		
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
	
	public void incoming(String command) {
		String[] args = command.split(";");
		
		switch (command) {
		case "disconnect":
			close();
			break;
		case "channel":
			type = ChannelType.valueOf(args[1].toUpperCase());
			break;
		}
	}
	
}
