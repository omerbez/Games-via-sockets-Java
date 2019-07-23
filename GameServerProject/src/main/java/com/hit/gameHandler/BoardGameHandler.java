package com.hit.gameHandler;

import com.hit.gameAlgo.GameBoard.GameMove;
import com.hit.gameAlgo.IGameAlgo;
import com.hit.gameAlgo.IGameAlgo.GameState;


public class BoardGameHandler 
{
	private IGameAlgo game;
	
	public BoardGameHandler(IGameAlgo game) {
		this.game = game;
	}
	
	public char[][]	computerStartGame() {
		game.calcComputerMove();
		return getBoardState();
	}
	
	public char[][]	getBoardState() {
		return game.getBoardState();
	}
	
	public GameState playOneRound(GameMove playerMove) {
		boolean r = game.updatePlayerMove(playerMove);
		if(!r)
			return GameState.ILLEGAL_PLAYER_MOVE;
		
		if(game.getGameState(null) == GameState.IN_PROGRESS) {
			game.calcComputerMove();
		}
		
		return game.getGameState(null);
	}
}