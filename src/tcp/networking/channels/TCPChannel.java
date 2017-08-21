package tcp.networking.channels;

import java.io.IOException;
import java.net.Socket;

import tcp.networking.TCPConnection;
import utils.Console;

/**
 *
 * represents an abstract channel on a connection
 *
 */
public abstract class TCPChannel {
	
	private String name;
	private Thread channel;
	
	protected Socket socket;
	protected TCPConnection con;
	protected Console console;
	protected boolean ready;
	
	public TCPChannel(String name) {
		this.name = name;
	}
	
	public void init(Socket socket, TCPConnection con, Console console) {
		this.socket = socket;
		this.con = con;
		this.console = console;
	}
	
	/**
	 * abstract methods
	 */
	
	abstract void createIO();
	abstract void closeIO();
	public abstract TCPChannelType getType();
	
	/**
	 * starts the channel
	 * - sets up IO-Streams
	 * - start incoming listener
	 */
	public void start() {
		channel = new Thread(new Runnable() {
			public void run() {
				console.debug("Started channel " + name + " on connection " + con.getIP());
				createIO();
			}
		});
		channel.setName("CHANNEL_"+name);
		channel.start();
	}
	
	/**
	 * closes the channel and closes IO-Streams
	 */
	public void stop() {
		try {
			channel.interrupt();
			closeIO();
			socket.close();
		} catch (IOException e) {
			console.error("Could not close socket for channel: "+name);
			e.printStackTrace();
		}
	}
	
	/**
	 * getters
	 */
	
	public String getName() {
		return name;
	}
	
	public void waitLoading() {
		while (!ready) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
