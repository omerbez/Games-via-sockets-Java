package com.hit.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hit.exception.UnknownIdException;
import com.hit.gameAlgo.GameBoard.GameMove;
import com.hit.gameAlgo.IGameAlgo.GameState;
import com.hit.services.GameServerController;


public class HandleRequest implements Runnable
{
	private GameServerController gsc;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public HandleRequest(Socket s, GameServerController controller) throws IOException {
		gsc = controller;
		writer = new PrintWriter(s.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	
	private void closeResources() {
		try {
			writer.close();
			reader.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			//ignored..
		}
	}
	
	private JSONArray boardToJsonArray(char[][] board) {
		if(board == null)
			return null;
		JSONArray ja = new JSONArray();
		for(int i=0; i<board.length; i++) {
			JSONArray row = new JSONArray();
			for(int j=0; j<board[i].length; j++)
				row.add(String.valueOf(board[i][j]));
			ja.add(row);
		}
		return ja;
	}
	
	private void sendResponse(JSONObject response) {
		writer.println(response.toJSONString());
		writer.flush();
	}
	
	private void sendErrorResponse(String msg, int id) {
		JSONObject response = new JSONObject();
		response.put("type", "error");
		response.put("ID", id);
		response.put("msg", msg);
		sendResponse(response);
	}
	
	private char[][] getBoard(int gameId) {
		try {
			return gsc.getBoardState(gameId);
		} catch(UnknownIdException ex) {
			return null;
		}
	}
	
	private char[][] tryComputerStartGame(int id) {
		try {
			return gsc.computerStartGame(id);
		} catch(UnknownIdException ex) {
			return null;
		}
	}
	
	private GameState tryUpdateMove(int id, GameMove move) {
		try {
			//play player move + computer move (one round)
			return gsc.updateMove(id, move);
		} catch(UnknownIdException ex) {
			return null;
		}
	}
	
	private void handleRequest(JSONObject jo) {
		String type = (String)jo.get("type");
		JSONObject response;
		switch(type) {
			case "New-Game": {
				String game = (String)jo.get("game");
				String opponent = (String)jo.get("opponent");
				int gameId = gsc.newGame(game, opponent);
				response = new JSONObject();
				response.put("type", "New-Game");
				response.put("ID", gameId);
				response.put("board", boardToJsonArray(getBoard(gameId)));
				sendResponse(response);
				break;
			}	
			
			case "Update-Move": {
				int id = ((Long)jo.get("ID")).intValue();
				int row = ((Long)jo.get("row")).intValue();
				int col = ((Long)jo.get("col")).intValue();
				GameState state = tryUpdateMove(id, new GameMove(row, col));
				if(state == null) {
					sendErrorResponse("Id was not found", id);
					return;
				}
				
				response = new JSONObject();
				response.put("type", "Update-Move");
				response.put("ID", id);
				response.put("state", state.ordinal());
				response.put("board", state==GameState.ILLEGAL_PLAYER_MOVE ? null : boardToJsonArray(getBoard(id)));
				sendResponse(response);
				break;
			}
			
			case "Start-Game": {
				int id = ((Long)jo.get("ID")).intValue();
				char[][] board = tryComputerStartGame(id);
				if(board == null) {
					sendErrorResponse("Id was not found", id);
					return;
				}
				
				response = new JSONObject();
				response.put("type", "Start-Game");
				response.put("ID", id);
				response.put("board", boardToJsonArray(board));
				sendResponse(response);
				break;
			}
			
			case "Stop-Game": {
				int id = ((Long)jo.get("ID")).intValue();
				try {
					gsc.endGame(id);
					closeResources();
				} catch(UnknownIdException ex) {
					sendErrorResponse("Id was not found", id);
				}				
				break;
			}
		}
	}
	
	@Override
	public void run() {
		try {
			String s;
			JSONParser jsonParser = new JSONParser();
			JSONObject jo;
			while(true) {
				s = reader.readLine();
				jo = (JSONObject)jsonParser.parse(s);
				handleRequest(jo);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			closeResources();
		}
	}
}