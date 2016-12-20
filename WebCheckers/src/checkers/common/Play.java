package checkers.common;

public class Play {
	private int fromRow, fromCol, toRow, toCol;
	
	public Play (int fromRow, int fromCol, int toRow, int toCol) {
		this.fromRow = fromRow;
		this.fromCol = fromCol;
		this.toRow = toRow;
		this.toCol = toCol;
	}
	
	public int getfromRow() {
		return fromRow;
	}
	
	public int getfromCol() {
		return fromCol;
	}
	
	public int gettoRow() {
		return toRow;
	}
	
	public int gettoCol() {
		return toCol;
	}
	
}
