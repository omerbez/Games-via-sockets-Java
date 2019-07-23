package com.hit.games;

import com.hit.gameAlgo.GameBoard;


public abstract class TicTacTow extends GameBoard
{
	protected final char PLAYER_SIGN = 'X';
	protected final char COMPUTER_SIGN = 'O';
	protected final char NO_SIGN = ' ';
	
	
	public TicTacTow() {
		super(3, 3);
		initializeTable();
	}
	
	public boolean updatePlayerMove(GameMove move) {
		if(isLegalMove(move)) {
			gameBoard[move.getRow()][move.getColumn()] = PLAYER_SIGN;
			updateGameState();
			return true;
		}
		return false;
	}
	
	private void initializeTable() {
		for(int i=0; i<gameBoard.length; i++) {
			for(int j=0; j<gameBoard[i].length; j++) 
				gameBoard[i][j] = NO_SIGN;
		}
	}
	
	boolean isLegalMove(GameMove move) {
		int r = move.getRow();
		int c = move.getColumn();
		if(r>=0 && r<3 && c>=0 && c<3 && gameBoard[r][c]==NO_SIGN)
			return true;
		return false;
	}
	
	/**
	* return the amount of player's signs and computer's signs
	* in 'r' row.
	*/
	SignCount getSignCountOfRow(int r, char[][] board) {		
		int playerCounter = 0,computerCounter = 0;

		for(int j=0; j<3; j++) {
			if(board[r][j] == PLAYER_SIGN)
				playerCounter++;
			else
				if(board[r][j] == COMPUTER_SIGN)
					computerCounter++;
		}
		return new SignCount(playerCounter, computerCounter);
	}
	
	/**
	* return the amount of player's signs and computer's signs
	* in 'c' Column.
	*/
	SignCount getSignCountOfCol(int c, char[][] board) {
		int playerCounter = 0,computerCounter = 0;

		for(int j=0; j<3; j++) {
			if(board[j][c] == PLAYER_SIGN)
				playerCounter++;
			else
				if(board[j][c] == COMPUTER_SIGN)
					computerCounter++;
		}
		return new SignCount(playerCounter, computerCounter);
	}
	
	/**
	* return the amount of player's signs and computer's signs
	* in specified diagonal.
	*/
	SignCount getSignCountDiagonal(DiagonalType dt, char[][] board) {
		int playerCounter = 0,computerCounter = 0;
		
		if(dt == DiagonalType.LEFT) {
			for(int i=0; i<3; i++) {
				if(board[i][i] == PLAYER_SIGN)
					playerCounter++;
				else
					if(board[i][i] == COMPUTER_SIGN)
						computerCounter++;
			}
		}
		else {
			for(int i=0; i<3; i++) {
				if(board[i][2-i] == PLAYER_SIGN)
					playerCounter++;
				else
					if(board[i][2-i] == COMPUTER_SIGN)
						computerCounter++;
			}
		}
		return new SignCount(playerCounter, computerCounter);
	}

	protected boolean isBoardFull() {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++)
				if(gameBoard[i][j] == NO_SIGN)
					return false;
		}
		return true;
	}
	
	/**
	* update the Game State, this method should be called after any move made
	* by player or computer.
	*/
	void updateGameState() {
		SignCount counter = null;
		
		//Rows check..
		for(int i=0; i<3; i++) {
			counter = getSignCountOfRow(i, gameBoard);
			if(counter.getPlayerCounter() == 3 || counter.getComputerCounter() == 3) {
				gameState = counter.getPlayerCounter() == 3 ? GameState.PLAYER_WON : GameState.PLAYER_LOST;
				return;
			}
		}
		
		//Cols check..
		for(int i=0; i<3; i++) {
			counter = getSignCountOfCol(i, gameBoard);
			if(counter.getPlayerCounter() == 3 || counter.getComputerCounter() == 3) {
				gameState = counter.getPlayerCounter() == 3 ? GameState.PLAYER_WON : GameState.PLAYER_LOST;
				return;
			}
		}
		
		//Diagonals check..
		for(DiagonalType dt : DiagonalType.values()) {
			counter = getSignCountDiagonal(dt, gameBoard);
			if(counter.getPlayerCounter() == 3 || counter.getComputerCounter() == 3) {
				gameState = counter.getPlayerCounter() == 3 ? GameState.PLAYER_WON : GameState.PLAYER_LOST;
				return;
			}
		}
		
		
		if(isBoardFull())
			gameState = GameState.TIE;
	}
	
	
	static class SignCount 
	{
		private int computer, player;
		
		public SignCount(int player, int computer) {
			this.player = player;
			this.computer = computer;
		}

		public int getComputerCounter() {
			return computer;
		}

		public int getPlayerCounter() {
			return player;
		}
	}
	
	
	 enum DiagonalType {
		LEFT, RIGHT;
	}
}
