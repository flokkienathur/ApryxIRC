package net.apryx.irc;

public class Apl {
	public static void main(String[] args) {
		IRCServer server = new IRCServer();
		server.listen(6667);
	}
}
