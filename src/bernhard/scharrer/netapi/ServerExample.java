package bernhard.scharrer.netapi;

import bernhard.scharrer.netapi.packet.Packet;
import bernhard.scharrer.netapi.server.Client;
import bernhard.scharrer.netapi.server.Console;
import bernhard.scharrer.netapi.server.NetAPI;
import bernhard.scharrer.netapi.server.Server;
import bernhard.scharrer.netapi.server.TrafficManager;
import bernhard.scharrer.netapi.server.WindowsConsole;

public class ServerExample {
	
	public static void main(String[] args) {
		
		final Console console = new WindowsConsole();
		
		Server server = NetAPI.start("4ahel.at", 7788, 7789, 1, new TrafficManager() {
			
			@Override
			public void receive(Client client, Packet packet) {
				
			}
			
			@Override
			public void receive(Client client, String message) {
				console.info("Incoming msg! ("+message+") echoing it...");
				client.send(message);
			}
			
			@Override
			public void disconnect(Client client) {
				console.info("Client disconnected: " + client.getIP());
			}
			
			@Override
			public void connect(Client client) {
				console.info("Client connected: " + client.getIP());
			}

			@Override
			public void receive(Client client, int[] data) {
				client.send(new int[] {data[0]++});
			}

			@Override
			public void receive(Client client, float[] data) {
				
			}
			
		}, console);
	}
	
}
