package bernhard.scharrer.netapi.server;

public interface TrafficManager {
	
	public void connect(Client client);
	public void disconnect(Client client);
	
	public void message(String message);
	public void packet(Packet packet);
	
}
