package com.apryx.client;

public interface ClientConnectionListener {
	public void onConnect(Client client);
	public void onDisconnect(Client client);
}
