package com.hit.games;



public class CatchTheBunnySmart extends CatchTheBunny
{

	public CatchTheBunnySmart(int x) {
		super(x);
	}

	@Override
	public void calcComputerMove() {
		
		GameMove bestMove = null, move;
		RankParams max = null, r;
		for(Direction dir : Direction.values()) {
			move = createMoveByDirection(computerPos, dir);
			if(!isLegalMove(move, computerPos))
				continue;
			
			r = getRankOf(move);
			if(max == null || r.isBetterThan(max)) {
				max = r;
				bestMove = move;
			}
		}
		computerPos.set(bestMove.getRow(), bestMove.getColumn());
		updateGameState();
	}
	
	private RankParams getRankOf(GameMove pos) {		
		int distance = Math.abs(pos.getRow()-playerPos.getRow()) + Math.abs(pos.getColumn()-playerPos.getColumn());	
		//distance from the nearest border.
		int borderDistance = getMinimumOf(pos.getRow()-0, 8-pos.getRow(), pos.getColumn()-0, 8-pos.getColumn());
		
		return new RankParams(distance, borderDistance);
	}
	
	private int getMinimumOf(int...values) {
		int min = values[0];
		for(int i=1; i<values.length; i++) {
			if(values[i] < min)
				min = values[i];
		}
		return min;
	}
	
	private static class RankParams
	{
		/**
		 * A class which holds the information required for ranking the state/game
		 */
		private int distanceFromPlayer, borderDistance;
		
		public RankParams(int playerDistance, int borderDistance) {
			this.distanceFromPlayer = playerDistance;
			this.borderDistance = borderDistance;
		}
		
		public boolean isBetterThan(RankParams rp) {
			if(distanceFromPlayer > rp.distanceFromPlayer)
				return true;
			
			if(distanceFromPlayer == rp.distanceFromPlayer && borderDistance > rp.borderDistance)
				return true; //if the distance from the player is equals, prefer the position which is the farest from border..
			
			return false;
		}
	}
}