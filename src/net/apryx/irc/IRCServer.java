package net.apryx.irc;

import com.apryx.client.Client;
import com.apryx.server.Server;

public class IRCServer extends Server{
	
	public void onInit(){
		System.out.println("Server started!");
	}
	
	@Override
	public void onConnect(Client client) {
		super.onConnect(client);
		System.out.println("Client connected!");
		
		//do init stuff
	}
	
	@Override
	public void onDisconnect(Client client) {
		super.onDisconnect(client);
		System.out.println("Client connected!");
		
		//do disconnect stuff
	}
	
	@Override
	public void onMessage(Client client, String message) {
		super.onMessage(client, message);
		
		//do message stuff
		System.out.println(message);
	}
	
	
}
