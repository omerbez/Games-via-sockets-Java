package com.hit.gameAlgo;


public abstract class GameBoard implements IGameAlgo
{
	protected char[][] gameBoard; 
	protected GameState gameState;
	
	
	public GameBoard(int rowLength, int colLength) {
		gameBoard = new char[rowLength][colLength];
		gameState = GameState.IN_PROGRESS;
	}

	public char[][]	getBoardState() {
		return getCopyBoard(); //return a copy of the board in order to keep the encapsulation..
	}
	
	public GameState getGameState(GameMove move) {
		return gameState;
	}
	
	private char[][] getCopyBoard() {
		/**
		 * Copy the board in order to keep it immutable to the rest of the world.
		 * This method will copy any 2-dimension array, even if it's rows length
		 * are not equals.
		 */
		char[][] temp = new char[gameBoard.length][];
		for(int i=0; i<gameBoard.length; i++) {
			temp[i] = new char[gameBoard[i].length]; 
			for(int j=0; j<gameBoard[i].length; j++)
				temp[i][j] = gameBoard[i][j];
		}
		return temp;
	}
	
	
	public static class GameMove
	{
		private int row, col;
		
		public GameMove(int row, int column) {
			this.row = row;
			this.col = column;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getColumn() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}

		public void setRow(int row) {
			this.row = row;
		}
		
		public void set(int r, int c) {
			row = r;
			col = c;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this==obj)
				return true; //self equivalent..
			
			if(!(obj instanceof GameMove)) 
				return false; //not same type..
			
			GameMove other = (GameMove)obj;
			return (this.row==other.row && this.col==other.col);
		}
	}
}