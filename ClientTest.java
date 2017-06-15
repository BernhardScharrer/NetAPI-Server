package test;

import networking.Connection;
import networking.DefaultConsole;
import networking.channels.StringChannel;

public class ClientTest {
	
	public static void main(String[] args) {
		
		Connection con = new Connection("localhost", 7777, new DefaultConsole());
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		con.addChannel(new StringChannel("TEST") {
			
			@Override
			protected void incoming(String msg) {
				
				System.out.println("IN: "+msg);
				
			}
		});
		
	}
	
}
