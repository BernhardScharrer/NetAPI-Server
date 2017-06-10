package networking.channel;

import java.net.Socket;

import networking.Connection;
import networking.Console;

public abstract class StringChannel extends ObjChannel {

	public StringChannel(String name, Socket socket, Connection con, Console console) {
		super(name, socket, con, console);
	}

	public void send(String string) {
		super.send(string);
	}
	
	@Override
	void recieve(Object obj) {
		if (obj instanceof String) incoming((String) obj);
	}
	
	@Override
	protected ChannelType getType() {
		return ChannelType.STRING;
	}
	
	protected abstract void incoming(String string);
	
}
