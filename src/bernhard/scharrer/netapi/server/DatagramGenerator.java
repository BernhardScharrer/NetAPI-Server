package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DatagramGenerator {

	private static final int INTEGER_PACKET = 0;
	private static final int FLOAT_PACKET = 1;
	private static final int BYTE_SIZE = 4;
	
	private static int length;
	
	private static DatagramSocket socket;
	private static DatagramPacket packet;
	private static boolean started = false;
	
	static void start(String ip, int port, int length) {
		
		DatagramGenerator.length = length;
		
		try {
			socket = new DatagramSocket(port, Inet4Address.getByName(ip));
			started = true;
		} catch (SocketException e) {
			// TODO
		} catch (UnknownHostException e) {
			// TODO
		}
		
	}
	
	private static void startListener() {
		new Thread(()-> {
			
			socket.receive(packet);
			
		});
	}
	
	static void send(int[] buffer) {
		if (started) {
			if (DatagramGenerator.length==buffer.length) {
				packet = new DatagramPacket(generateIntPacket(buffer), DatagramGenerator.length, socket.getRemoteSocketAddress());
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO
				}
			} else {
				// TODO
			}
		} else {
			// TODO
		}
	}
	
	private static byte[] generateIntPacket(int[] data) {
		byte[] buffer = new byte[(BYTE_SIZE*length)+1];
		int n = 1;
		buffer[0] = INTEGER_PACKET;
		for (int i : data) {
			convertInt(buffer, n++*BYTE_SIZE, i);
		}
		return buffer;
	}
	
	private static void generateFloatPacket(float[] data) {
		
	}
	
	private static void convertInt(byte[] buffer, int start, int value) {
		buffer[start] = (byte) (value >>> 24);
		buffer[start+1] = (byte) (value >>> 16);
		buffer[start+2] = (byte) (value >>> 8);
		buffer[start+3] = (byte) (value);
	}

	private static int converToInt(byte[] buffer) {
		return (buffer[3] < 0 ? buffer[3] + 256 : buffer[3])
				+ ((buffer[2] < 0 ? buffer[2] + 256 : buffer[2]) << 8)
				+ ((buffer[1] < 0 ? buffer[1] + 256 : buffer[1]) << 16)
				+ ((buffer[0] < 0 ? buffer[0] + 256 : buffer[0]) << 24);
	}

	private static byte[] convertFloat(float value) {
	    byte[] bytes = new byte[4];
	    ByteBuffer.wrap(bytes).putFloat(value);
	    return bytes;
	}

	private static float convertToFloat(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getFloat();
	}
	
}
