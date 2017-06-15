package networking.channels;

/**
 * 
 * represents the main channel of a connection.
 * this channel is used to inform the server about very
 * important messages like "disconnect" or "new channel".
 * 
 * you can't acces this channel through your connection.
 * if you want to make your own string channel than add
 * a new one to the connection.
 * 
 */
public class MainChannel extends StringChannel {

	public MainChannel() {
		super("MAIN");
	}

	@Override
	protected void incoming(String msg) {
		super.con.recieveFromServer(msg);
	}

}
