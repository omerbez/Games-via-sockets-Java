package com.hit.services;

import java.util.HashMap;
import java.util.Map;

import com.hit.exception.UnknownIdException;
import com.hit.gameAlgo.GameBoard.GameMove;
import com.hit.gameAlgo.IGameAlgo.GameState;
import com.hit.gameHandler.BoardGameHandler;
import com.hit.games.CatchTheBunnyRandom;
import com.hit.games.CatchTheBunnySmart;
import com.hit.games.TicTacTowRandom;
import com.hit.games.TicTacTowSmart;


public class GamesService 
{
	private Map<Integer, BoardGameHandler> gamesHandler;
	private int idGenerator;
	private int maxSize; //the capacity.. max number of parallel games.
	
	
	public GamesService(int capacity) {
		maxSize = capacity;
		gamesHandler = new HashMap<Integer, BoardGameHandler>();
		idGenerator = 0;
	}
	
	public int newGame(String gameType, String opponent) {
		synchronized(this) {
			//crit section - game id creation, must be synchronized..
			
			if(gamesHandler.size() >= maxSize)
				return -1;
			
			BoardGameHandler game = null;
			if(gameType.equals("Tic Tac Toe")) {
				game = new BoardGameHandler(opponent.equals("Smart") ? new TicTacTowSmart() : new TicTacTowRandom());
			} else {
				game = new BoardGameHandler(opponent.equals("Smart") ? new CatchTheBunnySmart(10) : new CatchTheBunnyRandom(10));
			}
			int id = idGenerator++;
			gamesHandler.put(id, game);
			return id;
		}
	}
	
	public GameState updateMove(Integer gameId, GameMove playerMove) throws UnknownIdException {
		BoardGameHandler game = gamesHandler.get(gameId);
		if(game == null)
			throw new UnknownIdException(new Throwable("Game id is not exists!"));
		
		GameState state = game.playOneRound(playerMove);
		return state;
	}
	
	public char[][] getBoardState(Integer gameId) throws UnknownIdException {
		BoardGameHandler game = gamesHandler.get(gameId);
		if(game == null)
			throw new UnknownIdException(new Throwable("Game id is not exists!"));
		
		return game.getBoardState();
	}
	
	public char[][] computerStartGame(Integer gameId) throws UnknownIdException {
		BoardGameHandler game = gamesHandler.get(gameId);
		if(game == null)
			throw new UnknownIdException(new Throwable("Game id is not exists!"));
		
		return game.computerStartGame();
	}
	
	public void endGame(Integer gameId) throws UnknownIdException {
		synchronized(this) {
			if(!gamesHandler.containsKey(gameId))
				throw new UnknownIdException(new Throwable("Game id is not exists!"));
			
			gamesHandler.remove(gameId);
		}
	}
}