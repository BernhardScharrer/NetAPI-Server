package tcp.networking;

import tcp.networking.channels.TCPByteChannel;
import tcp.networking.channels.Packet;
import tcp.networking.channels.TCPPacketChannel;
import tcp.networking.channels.TCPStringChannel;
import utils.ErrorType;

public interface TCPStreamManager {
	
	public void incoming(TCPConnection con, TCPStringChannel channel, String msg);
	public void incoming(TCPConnection con, TCPPacketChannel channel, Packet packet);
	public void incoming(TCPConnection con, TCPByteChannel channel, byte[] buffer);
	
	public void connect(TCPConnection con);
	public void disconnect(TCPConnection con, ErrorType error);
	
}
