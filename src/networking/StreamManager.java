package networking;

import networking.channel.PacketChannel;
import networking.channel.StringChannel;

public interface StreamManager {
	
	public void incomingString(Connection con, StringChannel channel, String command);
	public void incomingPacket(Connection con, PacketChannel channel, Packet packet);
	
}
