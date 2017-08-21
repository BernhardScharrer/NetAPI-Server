package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channels.Channel;
import networking.channels.PacketChannel;
import networking.channels.StreamManager;
import networking.channels.StringChannel;
import utils.Console;
import utils.ErrorType;
import utils.Packet;

public class Server {
	
	private String ip;
	private int port;
	private StreamManager manager;
	private Console console;
	
	private SocketListener listener;
	private List<Connection> cons = new ArrayList<>();
	
	public static int udp_port;
	
	/**
	 * 
	 * represents the server
	 * 
	 */
	public Server(String ip, int port, StreamManager manager, Console console) {
		
		console.info("TCP server is starting!");
		
		this.ip = ip;
		this.port = port;
		this.manager = manager;
		this.console = console;
		this.udp_port = port+1;
		this.listener = new SocketListener(this, console);
		
	}
	
	/**
	 * methods
	 */
	
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
			
			/*
			 * check if server got right connection
			 */
			
			Connection right = getRightConnection();
			if (right != null) {
				console.debug("Corrected connection from "+con.getUUID()+" to "+right.getUUID());
				con = right;
				Connection.clearUUID();
			}
			
			/*
			 * returns false if connection is not prepared
			 */
			if (!con.addChannel(socket)) {
				
				/*
				 * in this case setup second stream on same ip
				 */
				cons.add(new Connection(this, socket));
				
			}
			
		}
		
	}

	public void close() {
		
		listener.stop();
		
	}
	
	/**
	 * searches for the right connection if there are two connections on one ip
	 */
	private Connection getRightConnection() {
		console.debug("Searching for connection correction... (UUID: "+Connection.getNextUUID()+")");
		for (Connection con : cons) if (con.getUUID() == Connection.getNextUUID()) return con;
		return null;
	}
	
	public Connection getConnection(String ip) {
		for (Connection con : cons) if (con.getIP().equals(ip)) return con;
		return null;
	}
	
	/**
	 * sends a string to every one who owns this channel
	 */
	public void sendToAll(String channel_name, String msg) {
		for (Connection con : cons) {
			Channel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof StringChannel) {
				StringChannel schannel = (StringChannel) channel;
				schannel.send(msg);
			}
		}
	}
	
	/**
	 * sends a string to every one who owns this channel
	 * except for one connection
	 */
	public void sendToAllOut(String channel_name, String msg, Connection who2not) {
		for (Connection con : cons) {
			if (con.getUUID()==who2not.getUUID()) continue;
			Channel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof StringChannel) {
				StringChannel schannel = (StringChannel) channel;
				schannel.send(msg);
			}
		}
	}
	
	/**
	 * sends a packet to every one who owns this channel
	 */
	public void sendToAll(String channel_name, Packet packet) {
		for (Connection con : cons) {
			Channel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof PacketChannel) {
				PacketChannel pchannel = (PacketChannel) channel;
				pchannel.send(packet);
			}
		}
	}
	
	/**
	 * sends a string to every one who owns this channel
	 * except for one connection
	 */
	public void sendToAllOut(String channel_name, Packet packet, Connection who2not) {
		for (Connection con : cons) {
			if (con.getUUID()==who2not.getUUID()) continue;
			Channel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof PacketChannel) {
				PacketChannel pchannel = (PacketChannel) channel;
				pchannel.send(packet);
			}
		}
	}
	
	/**
	 * remove connection
	 */
	public void disconnect(Connection con, ErrorType error) {
		console.info(con.getIP() + " disconnected!");
		cons.remove(con);
		con.close();
		manager.disconnect(con, error);
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
	
	public StreamManager getStreamManager() {
		return manager;
	}
	
}
