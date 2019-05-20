package com.hit.ticTacTow;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.hit.gameAlgo.GameBoard.GameMove;
import com.hit.games.TicTacTowSmart;


class TicTacTowTest
{
	private TicTacTowSmart game;
	
	@BeforeEach
	void setUp() throws Exception {
		game = new TicTacTowSmart();
	}

	@AfterEach
	void tearDown() throws Exception {
		print();
	}
	
	@Test
	void testMove1() {
		game.updatePlayerMove(new GameMove(1, 1));
		game.calcComputerMove();
		char[][] gameBoard = game.getBoardState();
		assertEquals(' ', gameBoard[0][1], "Bad position was chosen");
		assertEquals(' ', gameBoard[1][0], "Bad position was chosen");
		assertEquals(' ', gameBoard[1][2], "Bad position was chosen");
		assertEquals(' ', gameBoard[2][1], "Bad position was chosen");
	}
	
	@Test
	void testMove2() {
		game.updatePlayerMove(new GameMove(0, 1));
		game.calcComputerMove();
		char[][] gameBoard = game.getBoardState();
		assertEquals(' ', gameBoard[1][0], "Bad position was chosen");
		assertEquals(' ', gameBoard[1][2], "Bad position was chosen");
		assertEquals(' ', gameBoard[2][1], "Bad position was chosen");
	}
	
	@Test
	void testMove3() {
		game.calcComputerMove();
		char[][] gameBoard = game.getBoardState();
		assertEquals(' ', gameBoard[0][1], "Bad position was chosen");
		assertEquals(' ', gameBoard[1][0], "Bad position was chosen");
		assertEquals(' ', gameBoard[1][2], "Bad position was chosen");
		assertEquals(' ', gameBoard[2][1], "Bad position was chosen");
	}
	
	private void print() {
		char[][] board = game.getBoardState();
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++)
				System.out.print("|"+board[i][j]);
			System.out.println("|");
		}
		System.out.println("-------------------------------------------------------");
	}
}