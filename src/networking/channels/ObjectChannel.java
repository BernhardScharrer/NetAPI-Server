package networking.channels;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

/**
 * represents an object channel
 */
public abstract class ObjectChannel extends Channel {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ObjectChannel(String name) {
		super(name);
	}
	
	/**
	 * methods
	 */
	
	void send(Object object) {
		try {
			out.writeObject(object);
			out.flush();
		} catch (IOException e) {
			console.error("Error while trying to sned object!");
			e.printStackTrace();
		}
	}
	
	/**
	 * implemented methods
	 */
	
	@Override
	void createIO() {
		
		Object obj = null;
		
		try {
			
			out = new ObjectOutputStream(super.socket.getOutputStream());
			in = new ObjectInputStream(super.socket.getInputStream());
			
			console.debug("Succesfully set up object stream! ("+super.getName()+")");
			
			while ((obj = in.readObject()) != null) {
				console.debug("Incoming object: " + obj.toString());
				recieve(obj);
			}
			
			console.debug("Succesfully set up Object-IO!");
			
		} catch (SocketException e) {
			console.warn("Stream broke down! ("+super.con.getIP()+")");
			con.disconnect();
		} catch (IOException e) {
			console.error("IO-Excpetion occured while object was incoming.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	void closeIO() {
		
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * abstract methods
	 */
	
	protected abstract void recieve(Object obj);
	
}
