package bernhard.scharrer.netapi.server;

import bernhard.scharrer.netapi.packet.Packet;

public interface TCPModul {
	
	public void connect(Client client);
	public void disconnect(Client client);
	
	public void receive(Client client, String message);
	public void receive(Client client, Packet packet);
	public void receive(Client client, int[] data);
	public void receive(Client client, float[] data);
	
}
