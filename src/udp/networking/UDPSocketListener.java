package udp.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import utils.Console;

public class UDPSocketListener {

	private Thread listener;
	private DatagramSocket socket;
	private DatagramPacket packet;
	
	public UDPSocketListener(UDPServer server) {
		
		Console console = server.getConsole();
		
		listener = new Thread(() -> {
			
			console.debug("UDP socket listener started!");
			try {
				
				socket = new DatagramSocket(server.getPort());
				packet = new DatagramPacket(new byte[server.getBufferLength()], server.getBufferLength());
				
				while (true) {
					socket.receive(packet);
					byte[] data = packet.getData();
				}
				
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		
		listener.start();
		
	}
	
}
