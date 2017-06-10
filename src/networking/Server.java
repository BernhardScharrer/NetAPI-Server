package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private String ip;
	private int port;
	private Console console;
	
	private ClientListener listener;
	private List<Connection> cons = new ArrayList<>();
	
	public Server(String ip, int port, Console console) {
		this.ip = ip;
		this.port = port;
		this.console = console;
		
		this.console.info("Server started! ("+ip+":"+port+")");
		
		listener = new ClientListener();
		listener.start(this);
	}

	public void newSocket(Socket socket) {
		
		Connection con = getConnection(socket.getInetAddress().getHostAddress());
		
		/**
		 * create new connection
		 */
		if (con == null) {
			
			
			
		}
		/**
		 * add channel to connection
		 */
		else {
			
		}
		
	}
	
	public Console getConsole() {
		return console;
	}
	
	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public Connection getConnection(String ip) {
		for (Connection con : cons) if (con.getIP().equals(ip)) return con;
		return null;
	}
	
	public void close() {
		listener.stop();
		for (Connection con : cons) con.close();
	}
	
}
