package bernhard.scharrer.netapi;

import bernhard.scharrer.netapi.packet.Packet;
import bernhard.scharrer.netapi.server.Client;
import bernhard.scharrer.netapi.server.NetAPI;
import bernhard.scharrer.netapi.server.TrafficManager;
import bernhard.scharrer.netapi.server.WindowsConsole;

public class ServerExample {
	
	public static void main(String[] args) {
		NetAPI.start(true, new WindowsConsole(true), "localhost", 7777, new TrafficManager() {
			
			@Override
			public void packet(Client client, Packet packet) {
				
			}
			
			@Override
			public void message(Client client, String message) {
				
			}
			
			@Override
			public void disconnect(Client client) {
				
			}
			
			@Override
			public void connect(Client client) {
				
			}
		});
	}
	
}
