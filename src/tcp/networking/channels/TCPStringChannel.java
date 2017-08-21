package tcp.networking.channels;

/**
 * represents a type of channel
 * which online accepts string transfer.
 */
public class TCPStringChannel extends TCPObjectChannel {

	public TCPStringChannel(String name) {
		super(name);
	}
	
	/**
	 * methods
	 */
	
	public void send(String msg) {
		super.send(msg);
	}
	
	protected void incoming(String obj) {
		con.getStreamManager().incoming(con, this, obj);
	}

	/**
	 * implemented methods
	 */
	
	@Override
	protected void recieve(Object obj) {
		if (obj instanceof String) incoming((String) obj);
		else console.error("Wrong type! (" + obj.getClass().toString() + ")");
	}

	@Override
	public TCPChannelType getType() {
		return TCPChannelType.STRING;
	}
	
}
