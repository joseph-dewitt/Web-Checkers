package checkers.common;

public class Play {
	private int fromRow, fromCol, toRow, toCol;
	
	public Play (int fromRow, int fromCol, int toRow, int toCol) {
		this.fromRow = fromRow;
		this.fromCol = fromCol;
		this.toRow = toRow;
		this.toCol = toCol;
	}
	
	int getfromRow() {
		return fromRow;
	}
	
	int getfromCol() {
		return fromCol;
	}
	
	int gettoRow() {
		return toRow;
	}
	
	int gettoCol() {
		return toCol;
	}
}
