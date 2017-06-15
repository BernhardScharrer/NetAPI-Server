package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.utils.Console;

public class Server {
	
	private String ip;
	private int port;
	private Console console;
	private SocketListener listener;
	
	private List<Connection> cons = new ArrayList<>();
	
	/**
	 * 
	 * represents the server
	 * 
	 */
	public Server(String ip, int port, Console console) {
		
		console.info("Server is starting!");
		
		this.ip = ip;
		this.port = port;
		this.console = console;
		this.listener = new SocketListener(this, console);
		
	}
	
	/**
	 * incoming socket from listener
	 */
	public void newSocket(Socket socket) {
		
		Connection con = getConnection(socket.getInetAddress().getHostAddress());
		
		/*
		 * build new connection
		 */
		if (con==null) {
			
			cons.add(new Connection(this, socket));
			
		}
		/*
		 * add channel to existing connection
		 */
		else {
			
			// TODO
			
		}
		
	}
	
	/**
	 * methods
	 */
	public void close() {
		
		listener.stop();
	}
	
	public Connection getConnection(String ip) {
		for (Connection con : cons) if (con.getIP().equals(ip)) return con;
		return null;
	}
	
	public void disconnect(Connection con) {
		console.info(con.getIP() + " disconnected!");
		cons.remove(con);
		con.close();
	}

	/**
	 * getters
	 */
	
	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public Console getConsole() {
		return console;
	}
	
}
