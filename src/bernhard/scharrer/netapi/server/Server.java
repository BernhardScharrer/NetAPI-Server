package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ServerSocketFactory;

public class Server {
	
	private Thread server;
	private String ip;
	private int port;
	private TrafficManager traffic;
	private Console console;
	
	private ServerSocket socket;
	private List<Client> clients;

	public Server(String ip, int port, TrafficManager traffic, Console console) {
		
		this.ip = ip;
		this.port = port;
		this.traffic = traffic;
		this.console = console;
		this.clients = new ArrayList<>();
		
		server = new Thread(()->{
			
			startListener();
			
		});
		
		server.start();
		
	}
	
	/**
	 * creates a new socket listener (also a socket)
	 * which handels sockets which are incoming from clients.
	 */
	private void startListener() {
		
		Socket new_socket;
		
		try {
			
			socket = ServerSocketFactory.getDefault().createServerSocket(port);
			
			if (console.isDebugging()) console.debug("Succesfully set up socket listener!");
			
			while (true) {
				
				/**
				 * waits for incoming socket
				 */
				
				try {
					new_socket = socket.accept();
					if (console.isDebugging()) console.debug("Incoming socket from: " + new_socket.getInetAddress().getHostAddress());
					newSocket(new_socket);
				} catch (IOException e){
					console.error("IO occuered!");
					e.printStackTrace();
					continue;
				}
				
			}
			
		} catch (UnknownHostException e) {
			console.error("Unknown host!");
			e.printStackTrace();
		} catch (IOException e1) {
			console.error("Could not setup socket listener!");
		} finally {
			cleanUp();
		}
		
	}
	
	/**
	 * @param new_socket
	 */
	private void newSocket(Socket socket) {
		clients.add(new Client(traffic, socket));
	}

	/**
	 * clean up everything
	 */
	void cleanUp() {
		
		/*
		 * destry socket listener
		 */
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				console.error("Could not close socket listener!");
			}
		}
		
		/*
		 * clean up clients
		 */
		if (clients!=null) {
			for (Client client : clients) {
				client.cleanUp();
			}
		}
		
		/*
		 * interrupt server thread
		 */
		if (server!=null&&server.isAlive()&&!server.isInterrupted()) {
			server.interrupt();
		}
		
	}
	
}
