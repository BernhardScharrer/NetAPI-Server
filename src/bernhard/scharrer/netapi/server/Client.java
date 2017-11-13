package bernhard.scharrer.netapi.server;

import java.net.Socket;

public class Client {
	
	private Channel channel;
	
	Client(TrafficManager manager, Socket socket) {
		
		channel = new Channel(manager, socket);
		
	}
	
	void cleanUp() {
		channel.cleanUp();
	}
	
}
