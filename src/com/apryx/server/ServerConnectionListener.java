package com.apryx.server;

import com.apryx.client.Client;

public interface ServerConnectionListener {
	public void onConnect(Server server, Client client);
	public void onDisconnect(Server server, Client client);
}
