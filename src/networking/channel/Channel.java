package networking.channel;

import java.net.Socket;

import networking.Connection;
import networking.Console;

public abstract class Channel {
	
	protected String name;
	protected Socket socket;
	protected Connection con;
	protected Console console;
	
	/**
	 *
	 * represents an abstract model of a channel
	 * 
	 */
	Channel(String name, Socket socket, Connection con, Console console) {
		
		this.name = name;
		this.console = console;
		
	}

	public String getName() {
		return name;
	}
	
	protected abstract void setup();
	protected abstract ChannelType getType();
	
	public abstract void close();
	
}
