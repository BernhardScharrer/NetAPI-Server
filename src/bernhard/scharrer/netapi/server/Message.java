package bernhard.scharrer.netapi.server;

class Message extends Packet {

	private static final long serialVersionUID = -6212716518373468437L;

	Message(String msg) {
		super("MESSAGE");
		super.addEntry("MSG", msg);
	}

}
