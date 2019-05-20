package com.hit.catchTheBunny;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hit.gameAlgo.GameBoard.GameMove;
import com.hit.games.CatchTheBunnySmart;

class CatchTheBunnyTest 
{
	private CatchTheBunnySmart game;

	@BeforeEach
	void setUp() throws Exception {
		game = new CatchTheBunnySmart(10);
	}

	@AfterEach
	void tearDown() throws Exception {
		print();
	}

	@Test
	void test() {
		char[][] board = game.getBoardState();
		GameMove playerPos = findPlayerPos(board);
		GameMove computerPos = findComputerPos(board);
		print();
		int distance = Math.abs(playerPos.getRow() - computerPos.getRow()) + 
				Math.abs(playerPos.getColumn() - computerPos.getColumn());
		
		game.calcComputerMove();
		board = game.getBoardState(); //get the new board state..
		computerPos = findComputerPos(board);
		int newDistance = Math.abs(playerPos.getRow() - computerPos.getRow()) + 
				Math.abs(playerPos.getColumn() - computerPos.getColumn());
		
		assertTrue(newDistance >= distance, "Computer got closer to the player");
	}
	
	private GameMove findPlayerPos(char[][] board) {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++)
				if(board[i][j] == 'P')
					return new GameMove(i, j);
		}
		return null;
	}
	
	private GameMove findComputerPos(char[][] board) {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++)
				if(board[i][j] == 'C')
					return new GameMove(i, j);
		}
		return null;
	}
	
	private void print() {
		char[][] board = game.getBoardState();
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++)
				System.out.print("|"+board[i][j]);
			System.out.println("|");
		}
		System.out.println("-------------------------------------------------------");
	}

}