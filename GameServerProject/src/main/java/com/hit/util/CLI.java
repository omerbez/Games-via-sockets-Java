package com.hit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CLI implements Runnable
{
	private Scanner scanner;
	private PrintStream ps;
	private List<PropertyChangeListener> observers;
	
	
	public CLI(InputStream in, OutputStream out) {
		scanner = new Scanner(in);
		ps = new PrintStream(out);
		observers = new ArrayList<PropertyChangeListener>();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		observers.add(pcl);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		observers.remove(pcl);
	}
	
	public void writeResponse(String response) {
		ps.println(response);
	}
	
	private void informObservers(PropertyChangeEvent event) {
		for(PropertyChangeListener observer : observers) {
			observer.propertyChange(event);
		}
	}
	
	@Override
	public void run() {
		String s;
		while(true) {
			ps.print("Enter command: ");
			s = scanner.next();
			if(s.equals(Commands.GAME_SERVER_CONFIG.toString())) {
				int capacity = scanner.nextInt();
				informObservers(new PropertyChangeEvent(this, s, -1, capacity));
				ps.println("Done...");
			}
			else
				if(s.equals(Commands.START.toString())) {
					informObservers(new PropertyChangeEvent(this, s, null, null));
					ps.println("Starting server...");
				}
				else
					if(s.equals(Commands.SHUTDWON.toString())) {
						informObservers(new PropertyChangeEvent(this, s, null, null));
						break;
					}
		}
		ps.println("CLI shutdown..");
	}
	
	
	public static enum Commands {
		GAME_SERVER_CONFIG, START, SHUTDWON;
	}
}