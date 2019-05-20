package com.hit.games;

import java.util.Random;

public class TicTacTowRandom extends TicTacTow
{
	
	@Override
	public void calcComputerMove() {
		
		if(gameState != GameState.IN_PROGRESS)
			return;
		
		Random random = new Random();
		int row, col;
		do {
			row = random.nextInt(3);
			col = random.nextInt(3);
		} while(!isLegalMove(new GameMove(row, col)));
		
		gameBoard[row][col] = COMPUTER_SIGN;
		updateGameState();
	}
}