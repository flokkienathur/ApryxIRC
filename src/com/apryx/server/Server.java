package com.apryx.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.apryx.client.Client;
import com.apryx.client.ClientConnectionListener;
import com.apryx.client.ClientListener;

public class Server implements ClientConnectionListener, ClientListener{
	
	private int port;
	private ServerSocket serverSocket;
	private boolean running = false;
	private List<Client> clients;
	private List<ServerConnectionListener> serverListeners;
	
	public Server(){
		clients = new ArrayList<Client>();
		serverListeners = new ArrayList<ServerConnectionListener>();
	}
	
	public void listen(int port){
		setPort(port);
		listen();
	}
	
	public void listen(){
		running = true;
		try{
			serverSocket = new ServerSocket(port);
			
			onInit();
			while(running){
				Socket socket = serverSocket.accept();
				
				Client client = new Client();
				client.addClientConnectionListener(this);
				client.async(socket);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void addClient(Client client){
		clients.add(client);
	}
	
	public void removeClient(Client client){
		clients.remove(client);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}

	public void onConnect(Client client) {
		
		client.addClientListener(this);
		
		addClient(client);
		
		invokeOnConnect(client);
	}

	public void onDisconnect(Client client) {
		client.removeClientListener(this);
		
		removeClient(client);
		
		invokeOnDisconnect(client);
	}
	
	public void broadcastExclusive(String s, Client cl){
		for(Client c : clients){
			if(c != cl)
				c.send(s);
		}
	}
	
	public void broadcast(String s){
		for(Client c : clients){
			c.send(s);
		}
	}
	
	public void addServerConnectionListener(ServerConnectionListener listener){
		serverListeners.add(listener);
	}
	
	public void removeServerConnectionListener(ServerConnectionListener listener){
		serverListeners.remove(listener);
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private void invokeOnConnect(Client client){
		for(ServerConnectionListener listener : serverListeners){
			listener.onConnect(this, client);
		}
	}
	
	private void invokeOnDisconnect(Client client){
		for(ServerConnectionListener listener : serverListeners){
			listener.onDisconnect(this, client);
		}
	}

	@Override
	public void onMessage(Client client, String message) {
		
	}
	
	public void onInit(){
		
	}
	
	

}
