package udp.networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import utils.UUIDGenerator;

public class UDPConnection {
	
	private DatagramSocket socket;
	private DatagramPacket packet;
	private int uuid;
	
	private UDPChannel channel;
	
	/**
	 * represents a logical connection to a client
	 * 
	 * @param server
	 * @param socket
	 * @param buffer_length
	 */
	public UDPConnection(UDPServer server, DatagramSocket socket, int buffer_length) {
		this.socket = socket;
		this.packet = new DatagramPacket(new byte[buffer_length], buffer_length);
		this.channel = new UDPChannel(server, this);
		this.uuid = UUIDGenerator.generate();
	}

	/**
	 * send string to client
	 * 
	 * @param msg
	 */
	public void send(String msg) {
		channel.send(msg);
	}
	
	/**
	 * @return uuid
	 */
	public int getUUID() {
		return uuid;
	}
	
	protected DatagramSocket getSocket() {
		return socket;
	}
	
	protected DatagramPacket getPacket() {
		return packet;
	}
	
	/**
	 * disconnect this connection from server
	 */
	public void disconnect() {
		if (channel != null) channel.close();
		if (socket != null) socket.close();
	}
	
}
