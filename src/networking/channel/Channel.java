package networking.channel;

import java.net.Socket;

import networking.Connection;
import networking.Console;

public abstract class Channel {
	
	protected String name;
	protected Socket socket;
	protected Connection con;
	protected Console console;
	
	private Thread channel;
	
	/**
	 *
	 * represents an abstract model of a channel
	 * 
	 */
	Channel(String name, Socket socket, Connection con, Console console) {
		
		this.name = name;
		this.console = console;
		this.socket = socket;
		this.con = con;
		
	}

	public String getName() {
		return name;
	}
	
	public void start() {
		channel = new Thread(new Runnable() {
			public void run() {
				setup();
			}
		});
		channel.start();
	}
	
	public void stop() {
		channel.interrupt();
		close();
	}
	
	protected abstract void setup();
	protected abstract ChannelType getType();
	
	abstract void close();
	
}
