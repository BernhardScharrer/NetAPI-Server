package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

public class ClientListener {
	
	private Thread listener;
	private ServerSocket server;
	
	public void start(Server server) {
		
		server.getConsole().info("Listener is going to start now...");
		listener = new Thread(new Runnable() {
			public void run() {
				
				try {
					
					ClientListener.this.server = ServerSocketFactory.getDefault().createServerSocket(server.getPort());
					
					Socket new_socket = null;
					
					while (true) {
						
						new_socket = ClientListener.this.server.accept();
						server.newSocket(new_socket);
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		listener.start();
		
	}
	
	public ServerSocket getSocket() {
		return server;
	}
	
	public void stop() {
		listener.interrupt();
	}
	
}
