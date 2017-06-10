package networking.channel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import networking.Connection;
import networking.Console;

abstract class ObjChannel extends Channel {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean ready;
	
	public ObjChannel(String name, Socket socket, Connection con, Console console) {
		super(name, socket, con, console);
		start();
	}

	@Override
	protected void setup() {
		
		Object obj = null;
		
		try {
			
			console.debug("Trying to setup IO...");
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			console.debug("Finished with IO-setup!");
			ready = true;
			
			while ((obj = in.readObject()) != null) {
				recieve(obj);
			}
			
			close();
			
		} catch (SocketException e) {
			console.info("Disconnect <-- " + con.getIP());
			close();
		} catch (IOException e) {
			console.error("IP-Exception");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			console.error("Incoming object is strange... (" + obj.toString() + ")");
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			if (in!=null) in.close();
			if (out!=null) out.close();
			socket.close();
		} catch (IOException e) {
			console.error("Could not close channel! ("+name+")");
			e.printStackTrace();
		}
	}
	
	abstract void recieve(Object obj);
	
	void send(Object object) {
		while(!ready) try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); };
		try {
			out.writeObject(object);
			out.flush();
		} catch (IOException e) {
			console.error("Error while sending obj! ("+object.toString()+")");
			e.printStackTrace();
		}
	}
	
}
