package checkers.common;

public class Pieces extends Message{
	int row;
	int col;
	SquarePlayer p;
	
	public Pieces (int row, int col, SquarePlayer p) {
		this.row = row;
		this.col = col;
		this.p = p;
	}
	
	public int getRow() { return row; }
	
	public int getCol() { return col; }
	
	public String getPlayer() { return p.toString(); }
}
