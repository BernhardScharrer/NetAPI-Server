package main;

import networking.Connection;
import networking.DefaultConsole;
import networking.Packet;
import networking.Server;
import networking.StreamManager;
import networking.channel.ByteChannel;
import networking.channel.PacketChannel;
import networking.channel.StringChannel;

public class TestServer {
	
	public static void main(String[] args) {
		
		new Server("localhost", 7777, new StreamManager() {
			
			@Override
			public void incomingString(Connection con, StringChannel channel, String command) {
				System.out.println(con.getIP() + "|" + channel.getName() + " ("+channel.getType().toString().toUpperCase()+") >>> " + command);
			}
			
			@Override
			public void incomingPacket(Connection con, PacketChannel channel, Packet packet) {
				System.out.println(con.getIP() + "|" + channel.getName() + " ("+channel.getType().toString().toUpperCase()+") >>> " + packet.getName());
			}

			@Override
			public void incomingByte(Connection con, ByteChannel channel, byte[] buffer) {
				
				
			}
		}, new DefaultConsole());
		
	}
	
}
