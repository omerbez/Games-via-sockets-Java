package com.hit.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class GamesClient 
{
	private Socket socket;
	private int serverPort;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public GamesClient(int serverPort) { 
		this.serverPort = serverPort;
	}
	
	public String sendMessage(String message, boolean hasResponse) {
		if(writer == null || reader == null)
			return null;
		
		writer.println(message);
		writer.flush();
		if(hasResponse) {
			//wait for response and return it..
			try {	
				String response = reader.readLine();
				return response;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public void connectToServer() {
		try {
			socket = new Socket("127.0.0.1", serverPort);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (Exception ex) {
			ex.printStackTrace();
			socket = null;
		}
	}
	
	public void closeConnection() {
		try {
			reader.close();
			writer.close();
			socket.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		if(socket != null && socket.isConnected() && !socket.isClosed())
			return true;
		return false;
	}
}