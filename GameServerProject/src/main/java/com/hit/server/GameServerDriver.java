package com.hit.server;

import com.hit.util.CLI;

public class GameServerDriver {
	
	public static void main(String[] args) {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server(34567);
		cli.addPropertyChangeListener(server);
		new Thread(cli).start();
	}
}