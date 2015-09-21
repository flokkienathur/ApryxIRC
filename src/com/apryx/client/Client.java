package com.apryx.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Runnable{
	
	private Socket socket;

	private ArrayList<ClientConnectionListener> connectionListeners;
	private ArrayList<ClientListener> messageListener;
	
	private HashMap<String, Object> attributes;
	
	private PrintWriter out;
	private BufferedReader in;
	
	public Client(){
		connectionListeners = new ArrayList<ClientConnectionListener>();
		messageListener = new ArrayList<ClientListener>();
		attributes = new HashMap<String, Object>();
	}
	
	public void async(){
		Thread t = new Thread(this, "Client Thread");
		t.start();
	}
	
	public void async(Socket socket){
		this.setSocket(socket);
		async();
	}
	
	public void setSocket(String ip, int port){
		try{
			InetAddress address = InetAddress.getByName(ip);
			Socket socket = new Socket(address, port);
			setSocket(socket);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void connect(Socket socket){
		this.setSocket(socket);
		listen();
	}
	
	private void listen(){
		try{
			String line;
			while((line = in.readLine()) != null){
				invokeOnMessage(line);
			}
			
		}catch(Exception e){
		}
		disconnect();
	}

	public void run() {
		listen();
	}
	
	public void send(String s){
		out.write(s);
		out.flush();
	}

	public void addClientConnectionListener(ClientConnectionListener listener){
		connectionListeners.add(listener);
	}
	
	public void removeClientConnectionListener(ClientConnectionListener listener){
		connectionListeners.remove(listener);
	}

	public void addClientListener(ClientListener listener){
		messageListener.add(listener);
	}
	
	public void removeClientListener(ClientListener listener){
		messageListener.remove(listener);
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void disconnect(){
		
		if(socket != null){
			try{
				if(socket.isConnected() || !socket.isClosed())
					socket.close();
				
			}catch(Exception e){}
			
			invokeOnDisconnect();
		}
		
	}

	public void setSocket(Socket socket) {
		if(socket == null)
			return;
		
		if(this.socket != null)
			disconnect();
		
		try {
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			invokeOnConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.socket = socket;
	}
	
	private void invokeOnMessage(String message){
		for(ClientListener listener : messageListener){
			listener.onMessage(this, message);
		}
	}

	private void invokeOnConnect(){
		for(ClientConnectionListener listener : connectionListeners){
			listener.onConnect(this);
		}
	}
	
	private void invokeOnDisconnect(){
		for(ClientConnectionListener listener : connectionListeners){
			listener.onDisconnect(this);
		}
	}
	
	public void setAttribute(String attr, Object value){
		attributes.put(attr, value);
	}
	
	public Object getAttribute(String attr){
		return attributes.get(attr);
	}
	
	public void setAttribute(Object value){
		attributes.put(value.getClass().getName(), value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Class<T> attr){
		Object value = attributes.get(attr.getName());
		if(value.getClass() == attr)
			return (T) value;
		else
			return null;
	}
	
}
