package bernhard.scharrer.netapi.server;

import java.net.Socket;

public class Client {
	
	private Channel channel;
	private Console console;
	
	Client(TrafficManager manager, Socket socket, Console console) {
		
		this.channel = new Channel(this, manager, socket, console);
		this.console = console;
		
	}
	
	void cleanUp() {
		channel.cleanUp();
		console.debug("Cleaning up client.");
	}
	
}
