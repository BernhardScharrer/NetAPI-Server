package bernhard.scharrer.netapi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bernhard.scharrer.netapi.packet.Message;
import bernhard.scharrer.netapi.packet.Packet;

class Channel {

	private Client client;
	private TrafficManager manager;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private Thread receiver;
	private Console console;
	
	public Channel(Client client, TrafficManager manager, Socket socket, Console console) {
		
		this.client = client;
		this.manager = manager;
		this.console = console;
		
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			
			startReceiver();
			
		} catch (IOException e) {
			console.warn("Could not bind streams to: "+client.getIP());
		}
		
	}
	
	/**
	 * @param send message to client
	 */
	void send(String message) {
		new Thread(()-> {
			
			try {
				out.writeObject(new Message(message));
				out.flush();
			} catch (IOException e) {
				console.error("Could not send message!");
				cleanUp();
			}
			
		}).start();
	}
	
	/**
	 * @param send packet to client
	 */
	void send(Packet packet) {
		new Thread(()-> {
			
			try {
				out.writeObject(packet);
				out.flush();
			} catch (IOException e) {
				console.error("Could not send message!");
				cleanUp();
			}
			
		}).start();
	}
	
	/**
	 * start receiving packets from clients
	 */
	private void startReceiver() {
		receiver = new Thread(()-> {
			
			Object obj;
			Packet packet;
			String msg;
			
			while (true) {
				try {
					obj = in.readObject();
					if (obj instanceof Packet) {
						packet = (Packet) obj;
						if (packet instanceof Message) {
							msg = (String) packet.getEntry("MSG");
							console.debug("Incoming message: "+msg);
							manager.message(client, msg);
							msg = null;
						} else {
							console.debug("Incoming packet: "+packet.getName());
							manager.packet(client, packet);
							packet = null;
						}
					} else {
						console.warn("Strange packet! (Object: "+obj.toString()+")");
					}
				} catch (ClassNotFoundException e) {
					console.warn("Unknown class! (Class: "+e.getClass().getName()+")");
					continue;
				} catch (IOException e) {
					console.error("Stream broke down. " + client.getIP());
					client.cleanUp();
					break;
				}
			}
			
		});
		
		receiver.start();
	}

	public void cleanUp() {
		receiver.interrupt();
		console.debug("Cleaning up channel.");
	}
	
}
