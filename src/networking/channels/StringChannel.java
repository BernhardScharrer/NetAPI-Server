package networking.channels;

/**
 * represents a type of channel
 * which online accepts string transfer.
 */
public abstract class StringChannel extends ObjectChannel {

	public StringChannel(String name) {
		super(name);
	}
	
	/**
	 * methods
	 */
	
	public void send(String msg) {
		super.send(msg);
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
	
	/**
	 * abstract methods
	 */
	
	protected abstract void incoming(String msg);
	
}
