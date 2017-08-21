package tcp.networking.channels;

import tcp.networking.TCPConnection;
import utils.ErrorType;
import utils.Packet;

public interface TCPStreamManager {
	
	public void incoming(TCPConnection con, TCPStringChannel channel, String msg);
	public void incoming(TCPConnection con, TCPPacketChannel channel, Packet packet);
	public void incoming(TCPConnection con, TCPByteChannel channel, byte[] buffer);
	
	public void connect(TCPConnection con);
	public void disconnect(TCPConnection con, ErrorType error);
	
}
