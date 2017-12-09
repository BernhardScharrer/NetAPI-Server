package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPChannel {

	private static final int INTEGER_PACKET = 0;
	private static final int FLOAT_PACKET = 1;
	private static final int BYTE_SIZE = 4;
	private static final int MAX_CLIENTS = 100;
	
	private static UDPChannel[] clients = new UDPChannel[MAX_CLIENTS];
	private static byte[] receive_buffer;
	
	private Client client;
	
	private InetAddress client_address;
	private static int uport;
	private static int buffer;
	
	private int[] idata;
	private float[] fdata;
	
	private static Console console;
	private static TrafficManager manager;
	private static DatagramSocket socket_receiving;
	private static DatagramSocket socket_sending;
	private static DatagramPacket receiving_packet;
	private static DatagramPacket send_packet;
	
	private static boolean started = false;
	private static Thread listener;
	
	public static void setup(TrafficManager manager, Console console, int uport, int buffer) {
		
		UDPChannel.manager = manager;
		UDPChannel.uport = uport;
		UDPChannel.console = console;
		UDPChannel.buffer = buffer;
		UDPChannel.receive_buffer = new byte[BYTE_SIZE*buffer+1];
		UDPChannel.receiving_packet = new DatagramPacket(receive_buffer, receive_buffer.length);
		
		try {
			socket_receiving = new DatagramSocket(uport);
			socket_sending = new DatagramSocket();
			startListener();
			started = true;
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	UDPChannel(Client client) {
		
		this.client = client;
		clients[client.getUUID()] = this;
		try {
			client_address = Inet4Address.getByName(client.getIP());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void startListener() {
		listener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						socket_receiving.receive(receiving_packet);
						System.out.println("Right here");
						receive_buffer = receiving_packet.getData();
						receive(null);
					}
				} catch (IOException e) {
					console.warn("Stream broke down!");
					cleanUp();
				}				
			}
		});
		
		listener.start();
	}
	
	private synchronized static void receive(UDPChannel channel) {
		
		if (receive_buffer[0]==INTEGER_PACKET) {
			
			channel.idata = new int[buffer];
			for (int i = 0;i<channel.idata.length;i++) {
				channel.idata[i] = channel.converToInt(receive_buffer, 1+i*BYTE_SIZE);
			}
			manager.receive(channel.client, channel.idata);
			
		} else if (receive_buffer[0]==FLOAT_PACKET) {
			
			channel.fdata = new float[buffer];
			// TODO generate float array
			manager.receive(channel.client, channel.fdata);
			
		}
		
	}

	void send(int[] data) {
		System.out.println("Trying to send");
		if (started) {
			if (buffer==data.length) {
				try {
					send_packet = new DatagramPacket(generateIntDatagram(data), BYTE_SIZE*buffer+1, client_address, uport);
					System.out.println("Sending packet!");
					socket_sending.send(send_packet);
					System.out.println("Sended!");
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
		byte[] datagram = new byte[(BYTE_SIZE*buffer)+1];
		int n = 0;
		datagram[0] = INTEGER_PACKET;
		for (int i : data) {
			convertInt(datagram, n++*BYTE_SIZE+1, i);
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
	
	static void cleanUp() {
		
		started = false;
		
		if (listener!=null&&listener.isAlive()) {
			listener.interrupt();
		}
		
		if (socket_receiving!= null&&!socket_receiving.isClosed()) {
			socket_receiving.close();
		}
		
		send_packet = null;
		receiving_packet = null;
		
	}
	
}
