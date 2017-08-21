package tcp.networking.channels;

public class TCPPacketChannel extends TCPObjectChannel {

	public TCPPacketChannel(String name) {
		super(name);
	}

	/**
	 * methods
	 */
	
	public void send(Packet packet) {
		super.send(packet);
	}
	
	/**
	 * implemented methods
	 */
	
	@Override
	protected void recieve(Object obj) {
		if (obj instanceof Packet) super.con.getStreamManager().incoming(con, this, (Packet) obj);
		else console.error("Wrong type: "+obj.getClass().getName());
	}

	@Override
	public TCPChannelType getType() {
		return TCPChannelType.PACKET;
	}

}
