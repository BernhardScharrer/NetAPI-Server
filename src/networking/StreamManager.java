package networking;

import networking.channels.ByteChannel;
import networking.channels.Packet;
import networking.channels.PacketChannel;
import networking.channels.StringChannel;

public interface StreamManager {
	
	public void incoming(Connection con, StringChannel channel, String msg);
	public void incoming(Connection con, PacketChannel channel, Packet packet);
	public void incoming(Connection con, ByteChannel channel, byte[] buffer);
	
	public void connect(Connection con);
	public void disconnect(Connection con);
	
}
