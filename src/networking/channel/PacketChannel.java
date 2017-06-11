package networking.channel;

import java.net.Socket;

import networking.Connection;
import networking.Console;
import networking.Packet;

public abstract class PacketChannel extends ObjChannel {

	public PacketChannel(String name, Socket socket, Connection con, Console console) {
		super(name, socket, con, console);
	}
	
	public void send(Packet packet) {
		super.send(packet);
	}
	
	@Override
	void recieve(Object obj) {
		if (obj instanceof Packet) incoming((Packet) obj);
	}
	
	protected abstract void incoming(Packet packet);
	
	@Override
	public ChannelType getType() {
		return ChannelType.PACKET;
	}
	
}
