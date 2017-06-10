package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channel.Channel;

public class Connection {
	
	private String ip;
	private List<Channel> channels = new ArrayList<>();
	
	public Connection(Socket socket) {
		
		this.ip = socket.getInetAddress().getHostAddress();
		
	}

	public String getIP() {
		return ip;
	}

	public void close() {
		for (Channel channel : channels) channel.close();
	}
	
}
