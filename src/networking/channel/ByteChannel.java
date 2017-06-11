package networking.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import networking.Connection;
import networking.Console;

public abstract class ByteChannel extends Channel {
	
	private InputStream in;
	private OutputStream out;
	private byte[] buffer;
	
	public ByteChannel(String name, Socket socket, Connection con, Console console, int size) {
		super(name, socket, con, console);
		this.buffer = new byte[size];
		start();
	}

	@Override
	protected void setup() {
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			while (in.read(buffer) != -1) {
				incoming(buffer);
			}
			
			con.close();
		} catch (IOException e) {
			console.error("Could not setup IO!");
			e.printStackTrace();
		}
	}

	@Override
	public ChannelType getType() {
		return ChannelType.BYTE;
	}
	
	protected abstract void incoming(byte[] buffer);

	@Override
	public void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			console.error("Could not close channel! ("+name+")");
			e.printStackTrace();
		}
	}

}
