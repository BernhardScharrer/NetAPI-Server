package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

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
		
		System.out.println("Binding new client: "+client.getIP());
		this.client = client;
		clients[client.getCUID()] = this;
		
	}
	
	private static void startListener() {
		listener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						UDPChannel.receive_packet = new DatagramPacket(receive_buffer, receive_buffer.length);
						socket.receive(receive_packet);
						receive_buffer = receive_packet.getData();
						receive();
					}
				} catch (IOException e) {
					console.warn("Stream broke down!");
					closeListener();
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
					channel.idata[i] = channel.convertToInt(receive_buffer, OFFSET+i*BYTE_SIZE);
				}
				manager.receive(channel.client, channel.idata);
				
			} else if (receive_buffer[0]==FLOAT_PACKET) {
				
				channel.fdata = new float[buffer];
				for (int f = 0;f<channel.fdata.length;f++) {
					channel.fdata[f] = channel.convertToFloat(receive_buffer, OFFSET+f*BYTE_SIZE);
				}
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
					send_packet = new DatagramPacket(generateIntDatagram(data), BYTE_SIZE*buffer+OFFSET, host);
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
	
	void send(float[] data) {
		if (started) {
			if (buffer==data.length) {
				try {
					send_packet = new DatagramPacket(generateFloatDatagram(data), BYTE_SIZE*buffer+OFFSET, host);
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
	
	private byte[] generateFloatDatagram(float[] data) {
		byte[] datagram = new byte[(BYTE_SIZE*buffer)+OFFSET];
		int n = 0;
		datagram[0] = FLOAT_PACKET;
		for (float f : data) {
			convertFloat(datagram, n++*BYTE_SIZE+OFFSET, f);
		}
		return datagram;
	}
	
	private void convertInt(byte[] buffer, int start, int value) {
		buffer[start] = (byte) (value >>> 24);
		buffer[start+1] = (byte) (value >>> 16);
		buffer[start+2] = (byte) (value >>> 8);
		buffer[start+3] = (byte) (value);
	}

	private int convertToInt(byte[] buffer, int start) {
		return (buffer[start+3] < 0 ? buffer[start+3] + 256 : buffer[start+3])
				+ ((buffer[start+2] < 0 ? buffer[start+2] + 256 : buffer[start+2]) << 8)
				+ ((buffer[start+1] < 0 ? buffer[start+1] + 256 : buffer[start+1]) << 16)
				+ ((buffer[start] < 0 ? buffer[start] + 256 : buffer[start]) << 24);
	}
	
	private void convertFloat(byte[] buffer, int start, float value) {
	    ByteBuffer.wrap(buffer, start, OFFSET).putFloat(value);
	}

	private float convertToFloat(byte[] bytes, int start) {
	    return ByteBuffer.wrap(bytes,start,OFFSET).getFloat();
	}
	
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

	void cleanUp() {
		clients[client.getCUID()] = null;
	}
	
	static void closeListener() {
		
		console.debug("Closing datagram listener.");
		
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
