package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class UDPChannel {

	private static final int INTEGER_PACKET = 0;
	private static final int FLOAT_PACKET = 1;
	private static final int BYTE_SIZE = 4;
	private static final int OFFSET = 4;
	private static final int MAX_CLIENTS = 100;
	
	private static UDPChannel[] clients = new UDPChannel[MAX_CLIENTS];
	private static byte[] receive_buffer;
	
	private Client client;
	
	private static int buffer;
	
	private int[] idata;
	private float[] fdata;
	
	private SocketAddress host;
	
	private static Console console;
	private static TrafficManager manager;
	private static DatagramSocket socket;
	private static DatagramPacket receive_packet;
	private static DatagramPacket send_packet;
	
	private static boolean started = false;
	private static Thread listener;
	
	public static void setup(TrafficManager manager, Console console, int uport, int buffer) {
		
		UDPChannel.manager = manager;
		UDPChannel.console = console;
		UDPChannel.buffer = buffer;
		UDPChannel.receive_buffer = new byte[BYTE_SIZE*buffer+OFFSET];
		UDPChannel.receive_packet = new DatagramPacket(receive_buffer, receive_buffer.length);
		
		try {
			socket = new DatagramSocket(uport);
			startListener();
			started = true;
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	UDPChannel(Client client) {
		
		this.client = client;
		clients[client.getCUID()] = this;
		
	}
	
	private static void startListener() {
		listener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						socket.receive(receive_packet);
						receive_buffer = receive_packet.getData();
						receive();
						
//						send_packet = new DatagramPacket(receive_buffer, buffer, receive_packet.getSocketAddress());
//						socket.send(send_packet);
						//TODO
//						receive(null);
					}
				} catch (IOException e) {
					console.warn("Stream broke down!");
					cleanUp();
				}				
			}
		});
		
		listener.start();
	}
	
	private void setHost(SocketAddress host) {
		this.host = host;
	}

	private synchronized static void receive() {
		
		int cuid = receive_buffer[1];
		UDPChannel channel = clients[cuid];
		
		if (channel != null) {
			channel.setHost(receive_packet.getSocketAddress());
			
			if (receive_buffer[0]==INTEGER_PACKET) {
				
				channel.idata = new int[buffer];
				for (int i = 0;i<channel.idata.length;i++) {
					channel.idata[i] = channel.converToInt(receive_buffer, OFFSET+i*BYTE_SIZE);
				}
				manager.receive(channel.client, channel.idata);
				
			} else if (receive_buffer[0]==FLOAT_PACKET) {
				
				channel.fdata = new float[buffer];
				// TODO generate float array
				manager.receive(channel.client, channel.fdata);
				
			}
		} else {
			
			console.error("Packet from unknown client! (" + receive_packet.getAddress().getHostAddress()+")");
			
		}
		
	}

	void send(int[] data) {
		if (started) {
			if (buffer==data.length) {
				try {
					send_packet = new DatagramPacket(generateIntDatagram(data), BYTE_SIZE*buffer+1, host);
					socket.send(send_packet);
				} catch (IOException e) {
					console.warn("Stream broke down!");
					cleanUp();
				}
			} else {
				console.warn("Can't send datagram! (length does not match to: "+buffer+")");
			}
		} else {
			console.error("Can't send datagrams before binding socket!");
		}
	}
	
	private byte[] generateIntDatagram(int[] data) {
		byte[] datagram = new byte[(BYTE_SIZE*buffer)+OFFSET];
		int n = 0;
		datagram[0] = INTEGER_PACKET;
		for (int i : data) {
			convertInt(datagram, n++*BYTE_SIZE+OFFSET, i);
		}
		return datagram;
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
	
	static synchronized int getFreeSlot() {
		for (int slot=0;slot<MAX_CLIENTS;slot++) {
			if (clients[slot] == null || !clients[slot].getClient().isConnected()) {
				clients[slot] = null;
				return slot;
			}
		}
		return -1;
	}
	
	private Client getClient() {
		return client;
	}

	static void cleanUp() {
		
		started = false;
		
		if (listener!=null&&listener.isAlive()) {
			listener.interrupt();
		}
		
		if (socket!= null&&!socket.isClosed()) {
			socket.close();
		}
		
		send_packet = null;
		receive_packet = null;
		
	}
	
}
