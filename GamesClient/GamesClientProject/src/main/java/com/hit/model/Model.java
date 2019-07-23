package com.hit.model;

public interface Model {
	void newGame(String gameType, String opponentType);
	void updatePlayerMove(int row, int col);
	void endGame();
	void computerStartGame();
	void restartGame();
}