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
	private int uport;
	private int buffer;
	private TrafficManager traffic;
	private Console console;
	
	private ServerSocket socket;
	private List<Client> clients;
	
	Server(String ip, int port, int uport, int buffer, TrafficManager traffic, Console console) {
		
		this.ip = ip;
		this.port = port;
		this.uport = uport;
		this.buffer = buffer;
		this.traffic = traffic;
		this.console = console;
		
		this.clients = new ArrayList<>();
		
		server = new Thread(new Runnable() {
			@Override
			public void run() {
				startListener();
			}
		});
		
		server.start();
		
		printHeadline();
		
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
					new_socket = null;
				} catch (IOException e){
					console.error("Socket listener broke down!");
					cleanUp();
					break;
				}
				
			}
			
		} catch (UnknownHostException e) {
			console.error("Unknown host!");
		} catch (IOException e) {
			console.error("Could not setup socket listener!");
		} finally {
			cleanUp();
		}
		
	}
	
	/**
	 * @param new_socket
	 */
	private void newSocket(Socket socket) {
		clients.add(new Client(traffic, socket, console, uport, buffer));
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
			clients.clear();
		}
		
		/*
		 * interrupt server thread
		 */
		if (server!=null&&server.isAlive()&&!server.isInterrupted()) {
			server.interrupt();
		}
		
	}
	
	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public Console getConsole() {
		return console;
	}
	
	private static void printHeadline() {
		System.out.println("\n ____  _____        _        _       _______  _____  ");
		System.out.println("|_   \\|_   _|      / |_     / \\     |_   __ \\|_   _| ");
		System.out.println("  |   \\ | |  .---.`| |-'   / _ \\      | |__) | | |   ");
		System.out.println("  | |\\ \\| | / /__\\\\| |    / ___ \\     |  ___/  | |   ");
		System.out.println(" _| |_\\   |_| \\__.,| |, _/ /   \\ \\_  _| |_    _| |_  ");
		System.out.println("|_____|\\____|'.__.'\\__/|____| |____||_____|  |_____| ");
		System.out.println("");
		System.out.println("Written by Bernhard Scharrer\n");
	}
	
}
