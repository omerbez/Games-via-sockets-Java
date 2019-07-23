package com.hit.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class GamesModel implements Model
{
	private List<PropertyChangeListener> observers;
	private GamesClient gameClient;
	private int gameID = -1;	
	private String lastGameType, lastOpponent; //for game restart
	private boolean playerStarts;
	
	
	public GamesModel() {
		observers = new ArrayList<PropertyChangeListener>();
		gameClient = new GamesClient(34567);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener observer) {
		observers.add(observer);
	}
	
	private void notifyObservers(PropertyChangeEvent event) {
		for(PropertyChangeListener observer : observers)
			observer.propertyChange(event);
	}
	
	private void createPropertyEvent(String response) {
		PropertyChangeEvent event;
		if(response == null)
			event = new PropertyChangeEvent(this, null, 0, 0);
		else
			event = new PropertyChangeEvent(this, response, 0, 0);
		
		notifyObservers(event);
	}
	
	public char[][] jsonArrayToBoard(JSONArray jsonArray) {
		char[][] board = new char[jsonArray.size()][];
		for(int i=0; i<board.length; i++) {
			JSONArray row = ((JSONArray)jsonArray.get(i));
			board[i] = new char[row.size()];
			for(int j=0; j<board[i].length; j++)
				board[i][j] = (((String)row.get(j)).equals("X") || ((String)row.get(j)).equals("P")) ? 'X' 
						: (((String)row.get(j)).equals("O") || ((String)row.get(j)).equals("C")) ? 'O' : ' ';
		}

		return board;
	}
	
	public void setGameId(int id) {
		gameID = id;
	}
	
	public String getLastGameType() {
		return lastGameType;
	}
	
	public void setLastStarts(boolean lastStarts) {
		this.playerStarts = lastStarts;
	}

	@Override
	public void newGame(String gameType, String opponentType) {
		if(!gameClient.isConnected())
			gameClient.connectToServer();
		
		lastGameType = gameType;
		lastOpponent = opponentType;
		
		JSONObject msg = new JSONObject();
		msg.put("type", "New-Game");
		msg.put("game", gameType);
		msg.put("opponent", opponentType);
		String response = gameClient.sendMessage(msg.toJSONString(), true);
		createPropertyEvent(response);
	}

	@Override
	public void updatePlayerMove(int row, int col) {
		if(!gameClient.isConnected())
			return;
		
		JSONObject msg = new JSONObject();
		msg.put("type", "Update-Move");
		msg.put("ID", gameID);
		msg.put("row", row);
		msg.put("col", col);
		String response = gameClient.sendMessage(msg.toJSONString(), true);
		createPropertyEvent(response);
	}

	@Override
	public void endGame() {
		JSONObject msg = new JSONObject();
		msg.put("type", "Stop-Game");
		msg.put("ID", gameID);
		gameClient.sendMessage(msg.toJSONString(), false);
		gameClient.closeConnection();
	}

	@Override
	public void computerStartGame() {
		JSONObject msg = new JSONObject();
		msg.put("type", "Start-Game");
		msg.put("ID", gameID);
		String response = gameClient.sendMessage(msg.toJSONString(), true);
		createPropertyEvent(response);
	}

	@Override
	public void restartGame() {
		newGame(lastGameType, lastOpponent);
		if(!playerStarts)
			computerStartGame();
	}
}