package test;

import networking.DefaultConsole;
import networking.Server;

public class TestServer {
	
	public static void main(String[] args) {
		
		Server server = new Server("localhost", 7777, new DefaultConsole());
		
		System.out.println("Test");
		
	}
	
}
