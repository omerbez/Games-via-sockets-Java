package com.hit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hit.model.Cell;
import com.hit.model.GamesModel;
import com.hit.model.Model;
import com.hit.view.GamesView;
import com.hit.view.View;

public class GamesController extends MouseAdapter implements Controller, ActionListener
{
	private View view;
	private Model model;

	public GamesController(Model model, View view) {
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		Cell c = ((GamesView)view).getCellByLocation(e.getX(), e.getY());
		if(c != null) {
			model.updatePlayerMove(c.getRow(), c.getCol());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("NEW_GAME")) {
			String gameType = ((GamesView)view).getChosenGameType();
			String opponentType = ((GamesView)view).getChosenOpponentType();
			boolean playerStarts = ((GamesView)view).getWhoStarts();
			((GamesModel)model).setLastStarts(playerStarts);
			((GamesView)view).prepareNewGame(gameType);
			model.newGame(gameType, opponentType);
			if(!playerStarts)
				model.computerStartGame();
		}
		else if(e.getActionCommand().equals("SHOW_GAME_CHOOSER")) {
			((GamesView)view).showGameChooser();
		}
		else if(e.getActionCommand().equals("FORCE_EXIT")) {
			System.exit(0);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		try {
			if(event == null || event.getPropertyName() == null)
				throw new Exception("ERROR");
			
			//View notifications
			if(event.getPropertyName().equals("END_GAME")) {
				model.endGame();
				return;
			}
			else if(event.getPropertyName().equals("RESTART_GAME")) {
				model.endGame();
				((GamesView)view).prepareNewGame(((GamesModel)model).getLastGameType());
				model.restartGame();
				return;
			}
			
			//Model notifications
			JSONParser parser = new JSONParser();
			JSONObject msg = (JSONObject)parser.parse(event.getPropertyName()); 
			
			if(msg.get("type").equals("New-Game")) {
				int id = ((Long)msg.get("ID")).intValue();
				JSONArray jsonBoard = (JSONArray)msg.get("board");
				if(jsonBoard == null)
					throw new Exception("Unexpected error occured, please try again");
				
				char[][] board = ((GamesModel)model).jsonArrayToBoard(jsonBoard);
				((GamesModel)model).setGameId(id);
				view.updateViewNewGame(board);
			}
			else 
				if(msg.get("type").equals("Update-Move")) {
					int state = ((Long)msg.get("state")).intValue();
					if(state != 0) {
						JSONArray jsonBoard = (JSONArray)msg.get("board");
						char[][] board = ((GamesModel)model).jsonArrayToBoard(jsonBoard);
						view.updateViewGameMove(state, board);
					}
				}
				else 
					if(msg.get("type").equals("Start-Game")) {
						JSONArray jsonBoard = (JSONArray)msg.get("board");
						char[][] board = ((GamesModel)model).jsonArrayToBoard(jsonBoard);
						view.updateViewGameMove(1, board);
					}
					else 
						if(msg.get("type").equals("error")) {
							throw new Exception("Error: "+msg.get("msg"));
						}
						else {
							//should not react here..
							throw new Exception("Unexpected error occured");
						}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}