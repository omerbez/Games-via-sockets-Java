package com.hit.services;

import com.hit.exception.UnknownIdException;
import com.hit.gameAlgo.GameBoard.GameMove;
import com.hit.gameAlgo.IGameAlgo.GameState;

public class GameServerController 
{
	private GamesService gamesService;
	
	
	public GameServerController(int capacity) {
		gamesService = new GamesService(capacity);
	}
	
	public int newGame(String gameType, String opponent) {
		return gamesService.newGame(gameType, opponent);
	}

	public void endGame(Integer gameId) throws UnknownIdException {
		gamesService.endGame(gameId);
	}
	
	public GameState updateMove(Integer gameId, GameMove playerMove) throws UnknownIdException {
		return gamesService.updateMove(gameId, playerMove);
	}
	
	public char[][] computerStartGame(Integer gameId) throws UnknownIdException {
		return gamesService.computerStartGame(gameId);
	}
	
	public char[][] getBoardState(Integer gameId) throws UnknownIdException {
		return gamesService.getBoardState(gameId);
	}
}