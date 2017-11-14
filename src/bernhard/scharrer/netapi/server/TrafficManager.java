package bernhard.scharrer.netapi.server;

public interface TrafficManager {
	
	public void connect(Client client);
	public void disconnect(Client client);
	
	public void message(Client client, String message);
	public void packet(Client client, Packet packet);
	
}
