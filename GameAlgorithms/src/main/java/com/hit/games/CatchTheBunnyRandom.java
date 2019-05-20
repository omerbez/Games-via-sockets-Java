package com.hit.games;

import java.util.Random;


public class CatchTheBunnyRandom extends CatchTheBunny
{

	public CatchTheBunnyRandom(int x) {
		super(x);
	}

	@Override
	public void calcComputerMove() {
		Random random = new Random();
		int dir; //0-3, up-left, clockwise..
		GameMove move;
		do {
			dir = random.nextInt(4);
			move = createMoveByDirection(computerPos, Direction.fromValue(dir));
		} while(!isLegalMove(move, computerPos));
		computerPos.set(move.getRow(), move.getColumn());
		updateGameState();
	}
}