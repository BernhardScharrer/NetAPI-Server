package bernhard.scharrer.netapi.packet;

public class Message extends Packet {

	private static final long serialVersionUID = -6212716518373468437L;

	public Message(String msg) {
		super("MESSAGE");
		super.addEntry("MSG", msg);
	}

}
