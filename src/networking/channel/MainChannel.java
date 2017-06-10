package networking.channel;

import java.net.Socket;

import networking.Connection;
import networking.Console;

public class MainChannel extends StringChannel {

	public MainChannel(Socket socket, Connection con, Console console) {
		super("MAIN", socket, con, console);
		console.debug("Startet MAIN-Channel!");
	}

	@Override
	protected void incoming(String command) {
		con.incoming(command);
	}

}
