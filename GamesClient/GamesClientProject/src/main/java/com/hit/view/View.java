package com.hit.view;


public interface View {
	public void start();
	public void updateViewNewGame(char[][] board);
	void updateViewGameMove(int gameState, char[][] board);
}