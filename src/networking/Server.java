package networking;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import networking.channel.ByteChannel;
import networking.channel.PacketChannel;
import networking.channel.StringChannel;

public class Server {
	
	private String ip;
	private int port;
	private Console console;
	private StreamManager manager;
	
	private ClientListener listener;
	private List<Connection> cons = new ArrayList<>();
	
	public Server(String ip, int port, StreamManager manager, Console console) {
		this.ip = ip;
		this.port = port;
		this.console = console;
		this.manager = manager;
		
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
			
			con = new Connection(this, socket, console);
			cons.add(con);
			
		}
		/**
		 * add channel to connection
		 */
		else {
			
			console.debug("create new channel: " + con.getNextChannelName() + " (" + con.getState() + ")");
			
			switch (con.getState()) {
			case BYTE:
				con.addChannel(new ByteChannel(con.getNextChannelName(), socket, con, console, con.getBufferSize()) {
					protected void incoming(byte[] buffer) {
						manager.incomingByte(con, this, buffer);
					}
				});
				break;
			case NONE:
				break;
			case PACKET:
				con.addChannel(new PacketChannel(con.getNextChannelName(), socket, con, console) {
					protected void incoming(Packet packet) {
						manager.incomingPacket(con, this, packet);
					}
				});
				break;
			case STRING:
				con.addChannel(new StringChannel("", socket, con, console) {
					protected void incoming(String command) {
						manager.incomingString(con, this, command);
					}
				});
				break;
			}
			
		}
		
	}
	
	public Console getConsole() {
		return console;
	}
	
	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public Connection getConnection(String ip) {
		for (Connection con : cons) if (con.getIP().equals(ip)) return con;
		return null;
	}
	
	public void sendAll(String msg) {
		console.debug("Outgoing command to all MAIN-Channels: " + msg);
		for (Connection con : cons) con.send(msg);
	}
	
	public void sendToAllOut(String msg, Connection connection) {
		console.debug("Outgoing command to all MAIN-Channels: " + msg);
		for (Connection con : cons) if (!con.getIP().equals(con.getIP())) con.send(msg);
	}
	
	public void close() {
		listener.stop();
		for (Connection con : cons) con.close();
	}
	
	public void disconnect(Connection con) {
		console.info("Client disconnected. ("+con.getIP()+")");
		cons.remove(con);
	}
	
}
