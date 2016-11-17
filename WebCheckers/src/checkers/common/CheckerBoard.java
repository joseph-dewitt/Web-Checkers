package checkers.common;

public class CheckerBoard {
	private Square board[][];
	static final public int BOARD_SIZE = 8;			// this is fixed and public; part of Checkers


	/**
	 *	Creates an empty board.
	 */
	
	public CheckerBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col] = new Square();	
			}
		}
	}

	/**
	 * Is a grid position valid?
	 * @param row row location of checkerboard
	 *@param ccol column location of checkerboard
	 * @return returns true if row & col fall within the dimensions of the checkerboard
	 */
	
	
	public boolean validLocation(int row, int col) {
		if (row < 0 || row >= BOARD_SIZE || 
				col < 0 || col >= BOARD_SIZE) {
			return false; 
		} else {
			return true;
		}
	}

	/**
	 * Black squares can be found in
	 *	Even Row & Odd Column
	 *	Odd Row & Even Column
	 * @param r row location of checkerboard
	 * @param c column location of checkerboard
	 * @return returns true if location (r,c) is a black square on the checkerboard
	 */
	
	public boolean squareIsBlack(int r, int c) {
		if (!validLocation(r, c))
			return false;
		if (r % 2 == 0)
			return (c % 2 != 0);
		return (c % 2 == 0);
	}

	
	/**
	 * @param r row location of checkerboard
	 * @param c column location of checkerboard
	 * @return returns true is there is a checker piece on the grid location (r,c)
	 *        otherwise returns false
	 */
	
	public boolean squareIsOccupied(int r, int c) {
		if (!validLocation(r,c))
			return false;
		return !(board[r][c].isEmpty());
	}
	
	/**
	returns true if the location is occupied by a King
	@param r row location of checkerboard
	@param c column location of checkerboard
	@return returns true if location is occupied by a King
	 */

	public boolean squareHoldsKing(int r, int c)
	{
		if (!validLocation(r,c) || !squareIsBlack(r,c) || !squareIsOccupied(r,c))
			return false;
		return (board[r][c].isKing());
	}
	
	/**
	 *	Determines who is at location (r,c)
	@param r is the row in the grid to check
	@param c is column in the gird to check
	@return returns true if "player one" is at location [r][c],
	        otherwise returns false
	 */
	public boolean isPlayerOne(int row, int col)
	{
		if (!validLocation(row,col))
			return false;
		return (board[row][col].getPlayer() == SquarePlayer.PlayerOne);

	}
	
	public boolean isKing(int r, int c) {
		return board[r][c].isKing();
	}

	public void makeKing(int r, int c) {
		board[r][c].makeKing();
	}
	
	public void set(int r, int c, SquarePlayer p) {
		board[r][c].setPlayer(p);
	}

	public SquarePlayer get(int r, int c) {
		return board[r][c].getPlayer();
	}
}
