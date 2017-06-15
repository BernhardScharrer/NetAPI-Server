package networking.channels;

/**
 * represents a type of channel
 * which online accepts string transfer.
 */
public class StringChannel extends ObjectChannel {

	public StringChannel(String name) {
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
	public ChannelType getType() {
		return ChannelType.STRING;
	}
	
}
