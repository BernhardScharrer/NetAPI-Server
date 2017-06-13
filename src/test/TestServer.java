package test;

import networking.DefaultConsole;
import networking.Server;

public class TestServer {
	
	public static void main(String[] args) {
		
		Server server = new Server("127.0.0.1", 7894, new DefaultConsole());
		
		System.out.println("Test");
		
	}
	
}
