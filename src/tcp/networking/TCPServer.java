package tcp.networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import tcp.networking.channels.TCPChannel;
import tcp.networking.channels.TCPPacketChannel;
import tcp.networking.channels.TCPStreamManager;
import tcp.networking.channels.TCPStringChannel;
import utils.Console;
import utils.ErrorType;
import utils.Packet;

public class TCPServer {
	
	private String ip;
	private int port;
	private TCPStreamManager manager;
	private Console console;
	
	private TCPSocketListener listener;
	private List<TCPConnection> cons = new ArrayList<>();
	
	/**
	 * 
	 * represents the server
	 * 
	 */
	public TCPServer(String ip, int port, TCPStreamManager manager, Console console) {
		
		console.info("TCP server is starting!");
		
		this.ip = ip;
		this.port = port;
		this.manager = manager;
		this.console = console;
		this.listener = new TCPSocketListener(this, console);
		
	}
	
	/**
	 * methods
	 */
	
	/**
	 * incoming socket from listener
	 */
	public void newSocket(Socket socket) {
		
		TCPConnection con = getConnection(socket.getInetAddress().getHostAddress());
		
		/*
		 * build new connection
		 */
		if (con==null) {
			
			cons.add(new TCPConnection(this, socket));
			
		}
		/*
		 * add channel to existing connection
		 */
		else {
			
			/*
			 * check if server got right connection
			 */
			
			TCPConnection right = getRightConnection();
			if (right != null) {
				console.debug("Corrected connection from "+con.getUUID()+" to "+right.getUUID());
				con = right;
				TCPConnection.clearUUID();
			}
			
			/*
			 * returns false if connection is not prepared
			 */
			if (!con.addChannel(socket)) {
				
				/*
				 * in this case setup second stream on same ip
				 */
				cons.add(new TCPConnection(this, socket));
				
			}
			
		}
		
	}

	public void close() {
		
		listener.stop();
		
	}
	
	/**
	 * searches for the right connection if there are two connections on one ip
	 */
	private TCPConnection getRightConnection() {
		console.debug("Searching for connection correction... (UUID: "+TCPConnection.getNextUUID()+")");
		for (TCPConnection con : cons) if (con.getUUID() == TCPConnection.getNextUUID()) return con;
		return null;
	}
	
	public TCPConnection getConnection(String ip) {
		for (TCPConnection con : cons) if (con.getIP().equals(ip)) return con;
		return null;
	}
	
	/**
	 * sends a string to every one who owns this channel
	 */
	public void sendToAll(String channel_name, String msg) {
		for (TCPConnection con : cons) {
			TCPChannel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof TCPStringChannel) {
				TCPStringChannel schannel = (TCPStringChannel) channel;
				schannel.send(msg);
			}
		}
	}
	
	/**
	 * sends a string to every one who owns this channel
	 * except for one connection
	 */
	public void sendToAllOut(String channel_name, String msg, TCPConnection who2not) {
		for (TCPConnection con : cons) {
			if (con.getUUID()==who2not.getUUID()) continue;
			TCPChannel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof TCPStringChannel) {
				TCPStringChannel schannel = (TCPStringChannel) channel;
				schannel.send(msg);
			}
		}
	}
	
	/**
	 * sends a packet to every one who owns this channel
	 */
	public void sendToAll(String channel_name, Packet packet) {
		for (TCPConnection con : cons) {
			TCPChannel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof TCPPacketChannel) {
				TCPPacketChannel pchannel = (TCPPacketChannel) channel;
				pchannel.send(packet);
			}
		}
	}
	
	/**
	 * sends a string to every one who owns this channel
	 * except for one connection
	 */
	public void sendToAllOut(String channel_name, Packet packet, TCPConnection who2not) {
		for (TCPConnection con : cons) {
			if (con.getUUID()==who2not.getUUID()) continue;
			TCPChannel channel = con.getChannel(channel_name);
			if (channel != null && channel instanceof TCPPacketChannel) {
				TCPPacketChannel pchannel = (TCPPacketChannel) channel;
				pchannel.send(packet);
			}
		}
	}
	
	/**
	 * remove connection
	 */
	public void disconnect(TCPConnection con, ErrorType error) {
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
	
	public TCPStreamManager getStreamManager() {
		return manager;
	}
	
}
