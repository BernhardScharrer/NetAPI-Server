package udp.networking;

import java.net.DatagramSocket;

import utils.ErrorType;

public interface UDPStreamManager {
	
	public void incoming(UDPConnection con, String msg);
	public void lostConnection(UDPConnection con, ErrorType error);
	public void bindFail(DatagramSocket socket);
	
}
