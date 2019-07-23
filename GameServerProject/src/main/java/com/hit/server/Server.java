package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.ServerSocket;
import java.net.Socket;

import com.hit.services.GameServerController;
import com.hit.util.CLI.Commands;


public class Server implements PropertyChangeListener, Runnable
{
	private ServerSocket serverSocket;
	private int capacity = 10; //default value..
	private Thread serverThread;
	private boolean shouldCancel = false;
	private GameServerController gsc;
	
	
	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if(e.getPropertyName().equals(Commands.GAME_SERVER_CONFIG.toString())) {
			capacity = (Integer)e.getNewValue();
		}
		else
			if(e.getPropertyName().equals(Commands.START.toString())) {
				startServer();
			}
			else
				if(e.getPropertyName().equals(Commands.SHUTDWON.toString())) {
					forceShutdown();
				}
	}

	private void startServer() {
		if(serverThread==null || !serverThread.isAlive()) {
			shouldCancel = false;
			gsc = new GameServerController(capacity);
			serverThread = new Thread(this);
			serverThread.start();
		}
	}
	
	private void forceShutdown() {
		/**
		 * block the thread until shutdown..
		 */
		if(serverThread!=null && serverThread.isAlive()) {
			shouldCancel = true;
			try {
				serverSocket.close();
			} catch(Exception ex) {
				//Ignore..
			}
		}
	}
	
	public void run() {
		while(!shouldCancel) {
			try {
				Socket socket = serverSocket.accept();
				HandleRequest hr = new HandleRequest(socket, gsc);
				new Thread(hr).start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Server has been shutdown!");
	}
}