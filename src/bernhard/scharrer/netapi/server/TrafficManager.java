package bernhard.scharrer.netapi.server;

import bernhard.scharrer.netapi.packet.Packet;

public interface TrafficManager {
	
	/**
	 * @param client
	 */
	public void connect(Client client);
	
	/**
	 * @param client
	 */
	public void disconnect(Client client);
	
	/**
	 * @param client
	 * @param message
	 */
	public void receive(Client client, String message);
	
	/**
	 * @param client
	 * @param packet
	 */
	public void receive(Client client, Packet packet);
	
	/**
	 * @param client
	 * @param data
	 */
	public void receive(Client client, int[] data);
	
	/**
	 * @param client
	 * @param data
	 */
	public void receive(Client client, float[] data);
	
}
