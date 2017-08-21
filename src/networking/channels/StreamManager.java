package networking.channels;

import networking.Connection;
import utils.ErrorType;
import utils.Packet;

public interface StreamManager {
	
	public void incoming(Connection con, StringChannel channel, String msg);
	public void incoming(Connection con, PacketChannel channel, Packet packet);
	public void incoming(Connection con, ByteChannel channel, byte[] buffer);
	
	public void connect(Connection con);
	public void disconnect(Connection con, ErrorType error);
	
}
