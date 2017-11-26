package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DatagramHandler {

	private final int INTEGER_PACKET = 0;
	private final int FLOAT_PACKET = 1;
	private final int BYTE_SIZE = 4;
	
	private int length;
	private byte[] receive_buffer;
	
	private DatagramSocket socket;
	private DatagramPacket receiving_packet;
	private DatagramPacket send_packet;
	private boolean started = false;
	private Thread listener;
	private Console console;
	private TrafficManager manager;
	private Client client;
	
	private int[] idata;
	private float[] fdata;
	
	DatagramHandler(Client client, TrafficManager manager, Console console, String ip, int port, int length) {
		
		this.client = client;
		this.manager = manager;
		this.console = console;
		this.length = length;
		this.receive_buffer = new byte[BYTE_SIZE*length+1];
		
		try {
			socket = new DatagramSocket(port, Inet4Address.getByName(ip));
			receiving_packet = new DatagramPacket(receive_buffer, receive_buffer.length);
			startListener();
			started = true;
		} catch (SocketException e) {
			// TODO
		} catch (UnknownHostException e) {
			// TODO
		}
		
	}
	
	private void startListener() {
		listener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						socket.receive(send_packet);
						receive_buffer = receiving_packet.getData();
						receive();
					}
				} catch (IOException e) {
					console.warn("Stream broke down!");
					cleanUp();
				}				
			}
		});
		
		listener.start();
	}
	
	private synchronized void receive() {
		
		if (receive_buffer[0]==INTEGER_PACKET) {
			
			idata = new int[length];
			for (int i = 0;i<idata.length;i++) {
				idata[i] = converToInt(receive_buffer, 1+i*BYTE_SIZE);
			}
			manager.receive(client, idata);
			
		} else if (receive_buffer[0]==FLOAT_PACKET) {
			
			fdata = new float[length];
			// TODO generate float array
			manager.receive(client, fdata);
			
		}
		
	}

	void send(int[] buffer) {
		if (started) {
			if (length==buffer.length) {
				try {
					send_packet = new DatagramPacket(generateIntPacket(buffer), BYTE_SIZE*length+1, socket.getRemoteSocketAddress());
					socket.send(send_packet);
				} catch (IOException e) {
					console.warn("Stream broke down!");
					cleanUp();
				}
			} else {
				console.warn("Can't send datagram! (length does not match to: "+length+")");
			}
		} else {
			console.error("Can't send datagrams before binding socket!");
		}
	}
	
	private byte[] generateIntPacket(int[] data) {
		byte[] buffer = new byte[(BYTE_SIZE*length)+1];
		int n = 1;
		buffer[0] = INTEGER_PACKET;
		for (int i : data) {
			convertInt(buffer, n++*BYTE_SIZE, i);
		}
		return buffer;
	}
	
//	private void generateFloatPacket(float[] data) {
//		
//	}
	
	private void convertInt(byte[] buffer, int start, int value) {
		buffer[start] = (byte) (value >>> 24);
		buffer[start+1] = (byte) (value >>> 16);
		buffer[start+2] = (byte) (value >>> 8);
		buffer[start+3] = (byte) (value);
	}

	private int converToInt(byte[] buffer, int start) {
		return (buffer[start+3] < 0 ? buffer[start+3] + 256 : buffer[start+3])
				+ ((buffer[start+2] < 0 ? buffer[start+2] + 256 : buffer[start+2]) << 8)
				+ ((buffer[start+1] < 0 ? buffer[start+1] + 256 : buffer[start+1]) << 16)
				+ ((buffer[start] < 0 ? buffer[start] + 256 : buffer[start]) << 24);
	}
//
//	private byte[] convertFloat(float value) {
//	    byte[] bytes = new byte[4];
//	    ByteBuffer.wrap(bytes).putFloat(value);
//	    return bytes;
//	}
//
//	private float convertToFloat(byte[] bytes) {
//	    return ByteBuffer.wrap(bytes).getFloat();
//	}
	
	void cleanUp() {
		
		started = false;
		
		if (listener!=null&&listener.isAlive()) {
			listener.interrupt();
		}
		
		if (socket!= null&&!socket.isClosed()) {
			socket.close();
		}
		
		send_packet = null;
		receiving_packet = null;
		
	}
	
}
