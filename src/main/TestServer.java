package main;

import networking.DefaultConsole;
import networking.Server;

public class TestServer {
	
	public static void main(String[] args) {
		
		new Server("localhost", 7777, new DefaultConsole());
		
	}
	
}
