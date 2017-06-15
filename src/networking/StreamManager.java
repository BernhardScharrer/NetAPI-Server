package networking;

import networking.channels.StringChannel;

public interface StreamManager {
	
	public void incoming(Connection con, StringChannel channel, String msg);
	
}
