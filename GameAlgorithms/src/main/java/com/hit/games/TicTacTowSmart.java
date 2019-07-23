package com.hit.games;


public class TicTacTowSmart extends TicTacTow
{
	
	@Override
	public void calcComputerMove() {		
		char[][] illustrationBoard = getBoardState();
		MoveQuality move = calcComputerBestMove(illustrationBoard);
		gameBoard[move.row][move.col] = COMPUTER_SIGN;
		updateGameState();
	}
	
	/**
	 * The "calcBestMove" method was separated into 2 methods ('calcComputerBestMove' and 'calcPlayerBestMove')
	 * In order to keep on "Single responsibility principle".
	 * in addition, the Recursion's stop-conditions has been changed from 'n' steps to full steps (until full board).
	 * 
	 * This method and 'calcPlayerBestMove' method calculates the computer best move by Recursion.
	 * It thinks ahead until game-over or until finding a "sure-move".
	 * note that the different between "getComputerBestMove()" and this method ("calcComputerBestMove()")
	 * is, that the first one does NOT thinks ahead - it returns only a "sure move" (winning move or losing move..)
	 * while the later one does..  
	 */
	private MoveQuality calcComputerBestMove(char[][] illustratedBoard) {	
		if(isBoardAlmostFull(illustratedBoard)) {
			MoveQuality m = getComputerBestMove(illustratedBoard);
			return m;
		}
		else {
			MoveQuality m = getComputerBestMove(illustratedBoard);
			if(m.rank == 100)
				return m; //find a sure move, no need to think ahead..
				
			//Think ahead and figure out the best move..
			MoveQuality maxMove = new MoveQuality();
				
			for(int i=0; i<3; i++) 
			{
				for(int j=0; j<3; j++) {
					if(illustratedBoard[i][j] == NO_SIGN) {
						illustratedBoard[i][j] = COMPUTER_SIGN;
							
						m = calcPlayerBestMove(illustratedBoard); //put sign and think ahead - what will be the player move?
						illustratedBoard[i][j] = NO_SIGN;
							
						if(m.rank > maxMove.rank)
							maxMove.set(i, j, m.rank);
					}
				}
			}	
			return maxMove;
		}
	}
	
	/**
	 * See 'calcComputerBestMove' method for indormation.
	 */
	private MoveQuality calcPlayerBestMove(char[][] illustratedBoard) {
		if(isBoardAlmostFull(illustratedBoard)) {
			MoveQuality m = getPlayerBestMove(illustratedBoard);
			return m;
		}
		else {
			MoveQuality m = getPlayerBestMove(illustratedBoard);
			if(m.rank == 0)
				return m; //find a sure move, no need to think ahead..
				
			//Think ahead and figure out the best move..
			MoveQuality minMove = new MoveQuality();
			minMove.rank = 101; //the default rank is -1 and we are looking for a minimum value..
			
			for(int i=0; i<3; i++)
			{
				for(int j=0; j<3; j++) {
					if(illustratedBoard[i][j] == NO_SIGN) {
						illustratedBoard[i][j] = PLAYER_SIGN;
						
						m = calcComputerBestMove(illustratedBoard); //put sign and think ahead - what will be the com move?
						illustratedBoard[i][j] = NO_SIGN;	
						
						if(m.rank < minMove.rank)
							minMove.set(i, j, m.rank);
					}
				}
			}
			return minMove;
		}
	}
	
	/**
	 * return true if the there is only 1 empty cell left in the board,
	 * false otherwise.
	 */
	private boolean isBoardAlmostFull(char[][] board) {		
		int counter = 0;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++)
				if(board[i][j] == NO_SIGN)
					counter++;
		}
		
		return counter==1;
	} 
	
	
	/**
	 * return the computer best move, based on thinking only one move ahead.
	 */
	private MoveQuality getComputerBestMove(char[][] board) {	
		int max = -1;
		GameMove bestMove = new GameMove(-1, -1);
		int r;
		
		Loops: {
			for(int i=0; i<3; i++) {
				for(int j=0; j<3; j++) {
					if(board[i][j] == NO_SIGN) {
						board[i][j] = COMPUTER_SIGN;
						
						r = rankBoard(board, false); //false because the computer already put a sign, so it's player turn now.
						if(r > max) {
							max = r;
							bestMove.set(i, j);
						}
						
						board[i][j] = NO_SIGN;	
						if(max == 100)
							break Loops;
					}
				}
			}
		}
		return new MoveQuality(bestMove, max);
	}
	
	/**
	 * Guess and return the player best move, based on thinking only one move ahead.
	 * The returned rank is the minimal! the rank is always associated to the computer..
	 * so the less computer rank - the more plater rank..
	 */
	private MoveQuality getPlayerBestMove(char[][] board) {		
		int min = 100; //The minimum of the computer board rank is best to the player..
		GameMove bestMove = new GameMove(-1, -1);
		int r;
		
		Loops: {
			for(int i=0; i<3; i++) {
				
				for(int j=0; j<3; j++) {
					if(board[i][j] == NO_SIGN) {
						board[i][j] = PLAYER_SIGN;
						
						r = rankBoard(board, true);
						if(r < min) {
							min = r;
							bestMove.set(i, j);
						}
						
						board[i][j] = NO_SIGN;
						if(min == 0)
							break Loops;
					}
				}
			}
		}
		return new MoveQuality(bestMove, min);
	}
	
	/**
	 * Calc and return the computer rank for the 'board' parameter,
	 * The board represent a move - done by computer or player.
	 * Parameters:
	 * 		board: the game board to calc the rank.
	 * 		comTurn: true if it's now the computer turn AND IT SHOULD PLAY NOW, false otherwise (mean that the computer
	 * 				 already played and now it's the player turn)
	 * 				 the board rank is affected by the turn, for example X|X| | is great
	 * 				 if the it's "X" turn now (He put X and win) but not that great if it's
	 * 			     "O" turn (put "O" at the top right corner and block..)
	 */
	private int rankBoard(char[][] board, boolean comTurn) {
		MutableInteger niceMoves = new MutableInteger(0); //of the computer or player, depends on "comTurn" param
		SignCount counter = null;
		int r;
		//rows check..
		for(int i=0; i<3; i++) {
			counter = getSignCountOfRow(i, board);
			r = getLoopResult(counter, niceMoves, comTurn);
			if(r==100 || r==0)
				return r;
		}
		
		//cols check..
		for(int i=0; i<3; i++) {
			counter = getSignCountOfCol(i, board);
			r = getLoopResult(counter, niceMoves, comTurn);
			if(r==100 || r==0)
				return r;	
		}
		
		//Diagonals check..
		for(DiagonalType dt : DiagonalType.values()) {
			counter = getSignCountDiagonal(dt, board);
			r = getLoopResult(counter, niceMoves, comTurn);
			if(r==100 || r==0)
				return r;
		}
		
		if(!comTurn)
			return niceMoves.getValue()>=2 ? 90 : niceMoves.getValue()==1 ? 70 : 50;
		else
			return niceMoves.getValue()>=2 ? 2 : 50;
	}
	
	/**
	 * Containts the Loops logic in order to reuse it
	 * instead of "copy paste"..
	 */
	private int getLoopResult(SignCount counter, MutableInteger niceCounter, boolean comTurn) {		
		if(!comTurn) {
			//Player turn now
			if(counter.getComputerCounter() == 3)
				return 100; //A win spot..
			
			if(counter.getPlayerCounter() == 2 && counter.getComputerCounter() == 0 )
				return 1; //Losing next turn, Must avoid the move..
			
			if(counter.getComputerCounter() == 2 && counter.getPlayerCounter() == 0) {
				niceCounter.increase(); //"nice" move.. the computer has 2 pieces in the row, and the player has nothing..
				return 70;
			}
		}
		else {
			//Computer turn now
			if(counter.getPlayerCounter() == 3)
				return 0; //Player will win!
			
			if(counter.getComputerCounter() == 2 && counter.getPlayerCounter() == 0) {
				return 99; //computer will win next turn
			}
			
			if(counter.getPlayerCounter() == 2 && counter.getComputerCounter() == 0) {
				niceCounter.increase(); //player's attack row..
				return 70;
			}
		}
		
		return 50;
	}
	
	
	/**
	 * Class which represent the move position and the rank of this move.
	 */
	private static class MoveQuality {
		private int row, col;
		private int rank;
		
		private MoveQuality() {
			row = col = rank = -1;
		}
		
		private MoveQuality(GameMove move, int r) {
			row = move.getRow();
			col = move.getColumn();
			rank = r;
		}
		
		private void set(int row, int col, int rank) {
			this.row = row;
			this.col = col;
			this.rank = rank;
		}
	}
}
