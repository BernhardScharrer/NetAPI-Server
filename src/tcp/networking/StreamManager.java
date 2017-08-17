package tcp.networking;

import tcp.networking.channels.ByteChannel;
import tcp.networking.channels.Packet;
import tcp.networking.channels.PacketChannel;
import tcp.networking.channels.StringChannel;

public interface StreamManager {
	
	public void incoming(Connection con, StringChannel channel, String msg);
	public void incoming(Connection con, PacketChannel channel, Packet packet);
	public void incoming(Connection con, ByteChannel channel, byte[] buffer);
	
	public void connect(Connection con);
	public void disconnect(Connection con, ErrorType error);
	
}
