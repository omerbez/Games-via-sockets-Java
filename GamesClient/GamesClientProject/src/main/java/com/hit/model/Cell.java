package com.hit.model;

public class Cell 
{
	private int row, col;
	
	public Cell(int x, int y) {
		row = y;
		col = x;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return "["+row+", "+col+"]";
	}
}