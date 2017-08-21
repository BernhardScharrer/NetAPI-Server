package udp.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import utils.Console;

public class UDPReceiver {
	
	private DatagramSocket socket;
	private DatagramPacket packet;
	private Thread receiver;
	private int buffer_length;
	private byte[] buffer;
	
	/**
	 * start udp receiver on ip:port
	 * 
	 * @param ip
	 * @param port
	 * @param buffer_length
	 * @param console
	 */
	public UDPReceiver(String ip, int port, int buffer_length, Console console) {
		
		console.info("Started UDP receiver on "+ip+":"+port);
		this.buffer_length = buffer_length;
		this.buffer = new byte[buffer_length];
		
		receiver = new Thread(() -> {
			
			try {
				socket = new DatagramSocket(port);
				while (true) {
					packet = new DatagramPacket(buffer, buffer_length);
					socket.receive(packet);
				}
				
			} catch (SocketException e) {
				console.error("Can't bind socket to "+ip+":"+port);
			} catch (IOException e) {
				console.error("IO-Exception occured at "+ip+":"+port);
			}
			
		});
		
		receiver.start();
		
	}
	
	/**
	 * @return buffer
	 */
	public byte[] buffer() {
		return buffer;
	}
	
	/**
	 * clear buffer
	 */
	public void clear() {
		for (int n = 0;n<buffer_length;n++) {
			buffer[n] = 0;
		}
	}
	
	/**
	 * @return string generated from buffer
	 */
	public String getMessage() {
		return new String(buffer);
	}
	
	/**
	 * stops listener and closes socket
	 */
	public void close() {
		if (receiver != null) receiver.interrupt();
		if (socket != null) socket.close();
	}
	
}
