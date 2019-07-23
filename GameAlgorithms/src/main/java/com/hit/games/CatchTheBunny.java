package com.hit.games;

import java.util.Random;

import com.hit.gameAlgo.GameBoard;



public abstract class CatchTheBunny extends GameBoard
{
	private int stepsLeft;
	GameMove playerPos, computerPos;
	
	
	public CatchTheBunny(int x) {
		super(9, 9);
		stepsLeft = x;
		playerPos = new GameMove(4, 4); 
		Random random = new Random();
		do {
			computerPos = new GameMove(random.nextInt(9), random.nextInt(9));
		}while(playerPos.equals(computerPos));
	}
	
	public boolean updatePlayerMove(GameMove move) {
		if(isLegalMove(move, playerPos)) {
			playerPos.set(move.getRow(), move.getColumn());
			stepsLeft--;
			updateGameState();
			return true;
		}
		return false;
	}
	
	@Override
	public char[][] getBoardState() {
		char[][] board = super.getBoardState();
		board[playerPos.getRow()][playerPos.getColumn()] = 'P';
		board[computerPos.getRow()][computerPos.getColumn()] = 'C';
		return board;
	}
	
	boolean isLegalMove(GameMove newPos, GameMove currentPos) {
		int r = newPos.getRow();
		int c = newPos.getColumn();
		int distance = Math.abs(currentPos.getRow()-r) + Math.abs(currentPos.getColumn()-c);
		if(r>=0 && r<9 && c>=0 && c<9 && distance==1)
			return true;
		return false;
	}
	
	void updateGameState() {
		
		if(stepsLeft == 0) 
			gameState = GameState.PLAYER_LOST;
		else
			if(playerPos.equals(computerPos))
				gameState = GameState.PLAYER_WON;
	}
	
	/**
	* Create and return 'GameMove' object which represent the new position after moving
	* one step from 'pos' by 'dir' direction
	* return null if 'pos' or 'dir' is null.
	*/	
	protected GameMove createMoveByDirection(GameMove pos, Direction dir) {	
		if(pos == null || dir==null)
			return null;

		switch(dir) {
			case UP: 
				return new GameMove(pos.getRow()-1, pos.getColumn());
			case RIGHT:
				return new GameMove(pos.getRow(), pos.getColumn()+1);
			case DOWN:
				return new GameMove(pos.getRow()+1, pos.getColumn());
			case LEFT:
				return new GameMove(pos.getRow(), pos.getColumn()-1);
			default:
				return null;
		}
	}
	
	enum Direction {	
		UP(0), RIGHT(1), DOWN(2), LEFT(3);		
		private int value;

		private Direction(int n) {
			value = n;
		}
		
		public int getValue() {
			return value;
		}
		
		public static Direction fromValue(int v) {
			for(Direction dir : values()) {
				if(dir.value == v)
					return dir;
			}
			return null;
		}
	}
}
