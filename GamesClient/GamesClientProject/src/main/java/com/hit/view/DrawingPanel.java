package com.hit.view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.hit.controller.GamesController;
import com.hit.model.Cell;


public class DrawingPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Image boardImage;
	private Image playerImage, comImage;
	private final int BOARD_SIZE = 396;
	private int SQUARE_SIZE = -1;
	private char[][] boardRef;
	
	
	public DrawingPanel(GamesController controller) {
		addMouseListener(controller);
	}
	
	public void prepareNewGame(String boardPath, String player, String com, int cellsNO) {
		SQUARE_SIZE = BOARD_SIZE/cellsNO;
		boardRef = null;
		try {
			boardImage = ImageIO.read(new File(boardPath));
			playerImage = ImageIO.read(new File(player));
			comImage = ImageIO.read(new File(com));
			boardImage = boardImage.getScaledInstance(BOARD_SIZE, BOARD_SIZE, Image.SCALE_SMOOTH);
			playerImage = playerImage.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
			comImage = comImage.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, Image.SCALE_SMOOTH);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void updateGameboard(char[][] board) {
		boardRef = board;
		repaint();
	}
	
	/**
	 * convert panel's mouse click into board cell.
	 * @param x mouse width point
	 * @param y mouse height point
	 * @return board cell or null if out of range.
	 */
	public Cell getCellByLocation(int x, int y) {
		int leftCornerX = getWidth()/2-BOARD_SIZE/2;
		int leftCornerY = getHeight()/2-BOARD_SIZE/2;
		if(x < leftCornerX || x > (leftCornerX+BOARD_SIZE) || y < leftCornerY || y > (leftCornerY+BOARD_SIZE))
			return null;
		
		return new Cell((x-leftCornerX)/SQUARE_SIZE, (y-leftCornerY)/SQUARE_SIZE);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(boardRef == null)
			return;
		
		//Draw the board..
		int x = getWidth()/2-BOARD_SIZE/2;
		int y = getHeight()/2-BOARD_SIZE/2;
		g.drawImage(boardImage, x, y, null);	
		
		//Draw the pieces..
		Image temp;
		for(int i=0; i<boardRef.length; i++) {
			for(int j=0; j<boardRef[i].length; j++) {
				if(boardRef[i][j] == 'X' || boardRef[i][j] == 'O') {
					temp = (boardRef[i][j] == 'X') ? playerImage : comImage;
					g.drawImage(temp, x + j*SQUARE_SIZE, y+i*SQUARE_SIZE, null);
				}
			}
		}
	}
}