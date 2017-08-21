package udp.networking;

import java.io.IOException;

import utils.ErrorType;

public class UDPChannel {
	
	private UDPServer server;
	private UDPConnection connection;
	private Thread listener;

	/**
	 * controlls data flow between server and client
	 * 
	 * @param server
	 * @param connection
	 */
	public UDPChannel(UDPServer server, UDPConnection connection) {
		this.server = server;
		this.connection = connection;
		
		this.listener = new Thread(() -> {
			try {
				while (true) {
					this.connection.getSocket().receive(this.connection.getPacket());
					recieve(this.connection.getPacket().getData());
				}
			} catch (IOException e) {
				server.getManager().lostConnection(connection, ErrorType.IO);
			} finally {
				connection.disconnect();
			}
		});
		
		this.listener.start();
		
	}

	/**
	 * triggered when data from client comes in
	 * 
	 * @param data
	 */
	public void recieve(byte[] data) {
		server.getManager().incoming(connection, new String(data));
	}
	
	/**
	 * send string to client
	 * 
	 * @param msg
	 */
	public void send(String msg) {
		try {
			connection.getPacket().setData(msg.getBytes());
			connection.getSocket().send(connection.getPacket());
		} catch (IOException e) {
			server.getManager().lostConnection(connection, ErrorType.IO);
		} finally {
			connection.disconnect();
		}
	}
	
	/**
	 * stop recieve listener
	 */
	public void close() {
		if (listener != null) listener.interrupt();
	}
	
}
