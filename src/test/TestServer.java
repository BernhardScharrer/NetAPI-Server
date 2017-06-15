package test;

import networking.Connection;
import networking.DefaultConsole;
import networking.Server;
import networking.StreamManager;
import networking.channels.StringChannel;

public class TestServer {
	
	public static void main(String[] args) {
		
		Server server = new Server("localhost", 7777, new StreamManager() {
			
			public void incoming(Connection con, StringChannel channel, String msg) {
				
				System.out.println("---------- Stream msg: " + msg);
				channel.send("got it!");
				
			}
			
		}, new DefaultConsole());
		
	}
	
}
