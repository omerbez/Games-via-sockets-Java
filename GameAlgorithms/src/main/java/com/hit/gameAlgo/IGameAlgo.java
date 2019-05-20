package com.hit.gameAlgo;

public interface IGameAlgo 
{
	public void	calcComputerMove();
	public char[][]	getBoardState();
	public GameState getGameState(GameBoard.GameMove move);
	public boolean updatePlayerMove(GameBoard.GameMove move);
	
	
	static enum GameState {
		ILLEGAL_PLAYER_MOVE, IN_PROGRESS, PLAYER_LOST, PLAYER_WON, TIE;
	}
}