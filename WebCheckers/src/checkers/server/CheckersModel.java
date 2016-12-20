package checkers.server;

import checkers.common.*;

import java.io.IOException;
import java.util.Observable;
import javax.websocket.*;



/**
 *
 * This is the Model - CheckersModel.java
 */

public class CheckersModel extends Observable { 

	private CheckerBoard board;					// grid representing the board

	private Session player1;
	private Session player2;
	private SquarePlayer turn = SquarePlayer.valueOf("PlayerOne");
	/**
	 *	Creates a board with all the pieces in their starting places.
	 */
	
	public CheckersModel(Session player1, Session player2) {
		this.player1 = player1;
		this.player2 = player2;
		board = new CheckerBoard();

		setPlayers();
	}

	public void newGame() {
		clearBoard();
		setPlayers();

		setChanged();
		notifyObservers();	
	}

	/**
	 * Sets all the board locations to empty
	 */
	private void clearBoard() {
		for (int r = 0; r < CheckerBoard.BOARD_SIZE; r++)
			for (int c = 0; c < CheckerBoard.BOARD_SIZE; c++)
				placePiece(r, c, SquarePlayer.Empty);
	}

	/**
	 *	Places the check board pieces in their starting locations
	 */
	private void setPlayers() {
		int startingPos;

		// Top player pieces in first three rows
		for (int row = 0; row < 3; row++) {		
			startingPos = (row % 2 == 0) ? 1 : 0; // 'even' rows have checkers in the odd squares, and vice-versa

			for (int col = startingPos; col < CheckerBoard.BOARD_SIZE; col = col + 2) {	// every other square
				placePiece(row, col, SquarePlayer.PlayerOne); 
			}
		}

		// Bottom player pieces in bottom three rows; same idea as above
		for (int row = CheckerBoard.BOARD_SIZE - 3; row < CheckerBoard.BOARD_SIZE; row++) {
			startingPos = (row % 2 == 0) ? 1 : 0; 

			for (int col = startingPos; col < CheckerBoard.BOARD_SIZE; col = col + 2) {
				placePiece(row, col,SquarePlayer.PlayerTwo); 
			}
		}
	}



	/**
	 * Are two positions occupied by different players?
	@param myRow row location of checkerboard
	@param myCol column location of checkerboard
	@param oppRow row location of checkerboard
	@param oppCol column location of checkerboard
	@precondition: Both positions are valid (within grid and on black)
	@return Returns true if positions occupy different players
	 */
	public boolean isOpponent(int myRow, int myCol, int oppRow, int oppCol) {
		if (!board.squareIsOccupied(myRow, myCol) || !board.squareIsOccupied(oppRow, oppCol))
			return false;

		return (board.get(myRow, myCol) != board.get(oppRow,oppCol));
	}



	/**
	 *	Checking to see if (fromRow,fromCol) to (toRwo,toCol)
	 *	is a valid non-jumping move.
	@param fromRow row location of checkerboard
	@param fromCol column location of checkerboard
	@param toRow row location of checkerboard
	@param toCol column location of checkerboard
	Precondition:  Current location is valid
	@return returns true if it is a valid move
	 */
	public boolean moveIsValid(int fromRow, int fromCol, int toRow, int toCol)
	{
		// Must move within the board
		if (!board.validLocation(toRow, toCol))
			return false;

		// Must move to a black square that is not occupied
		if (!board.squareIsBlack(toRow, toCol) && !board.squareIsOccupied(toRow, toCol))
			return false;
		
		// The column distance must be one
		if (Math.abs(fromCol - toCol) != 1)
			return false;


		/* If not a King must move 'forward' one row:
		 * Player one goes from the top to the bottom
		 * Player two goes from the bottom to the top
		 */

		if (!board.squareHoldsKing(fromRow,fromCol)) {
			if (board.get(fromRow,fromCol) == SquarePlayer.PlayerOne) {	// PLAYER_ONE moves 'down' the board			
				if (fromRow + 1 != toRow)
					return false;
			}
			else if (board.get(fromRow,fromCol) == SquarePlayer.PlayerTwo) {	// PLAYER_ONE moves 'up' the board
				if (fromRow - 1 != toRow)
					return false;
			}
		}
		else { // Kings can move 'forward' or 'backwards' one row
			if (Math.abs(fromRow - toRow) != 1)
				return false;
		}
		System.out.println("The move is valid");
		return true;
	}

	/**
	 *	Checking to see if (fromRow,fromCol) to (toRwo,toCol)
	 *	is a valid jump move.
	@param fromRow row location of checkerboard
	@param fromCol column location of checkerboard
	@param toRow row location of checkerboard
	@param toCol column location of checkerboard
	Precondition:  Current location is valid
	@return returns true if it is a valid jump move
	 */
	public boolean jumpIsValid(int fromRow, int fromCol, int toRow, int toCol)
	{
		// Must move within the board
		if (!board.validLocation(toRow, toCol)){
			System.out.println("Not a valid jump");
			return false;
		}

		// Must move to a black square that is not occupied
		if (!board.squareIsBlack(toRow, toCol) || board.squareIsOccupied(toRow, toCol)) {
			System.out.println("Must move to a black square that is not occupied");
			return false;
		}

		// It not a King must move forward
		// Player one goes from the top to the bottom
		// Player two goes from the bottom to the top
		// Check moving two spaces forward
		if (!board.squareHoldsKing(fromRow, fromCol)) {

			if (board.get(fromRow,fromCol) == SquarePlayer.PlayerOne) {
				if (fromRow + 2 != toRow) {	// Must move exactly two rows forward on a jump (down board)
					return false;
				}
				if (isOpponent(fromRow, fromCol, fromRow+1, (fromCol+toCol)/2)) { // Make sure there is an opponent in-between
					return true;
				}
				
				return false;
			}
			
			else { // Must be PlayerTwo
				
				if (fromRow - 2 != toRow)	// Must move exactly two rows forward on a jump (up board)
					return false;

				if (isOpponent(fromRow, fromCol, fromRow-1, (fromCol+toCol)/2)) // Make sure there is an opponent  in-between
					return true;
				
				return false;
			}
		}
		else	// Check for valid King move
		{

			if (fromRow - 2 != toRow && fromRow + 2 != toRow)	// Must move exactly two rows either forward or backwards
				return false;

			if (isOpponent(fromRow, fromCol, (fromRow+toRow)/2, (fromCol+toCol)/2)) // Make sure there is an opponent in between
				return true;
			return false;
		}
	}

	/**
	Attempt to move a checker piece
	@param fromRow row location of checkerboard
	@param fromCol column location of checkerboard
	@param toRow row location of checkerboard
	@param toCol column location of checkerboard
	@return returns true if a move has been made
	 */
	public boolean move(int fromRow, int fromCol, int toRow, int toCol, SquarePlayer client) {
		
		//check if it's their turn
		//if the move is made, switch whose turn it is
		if (client != turn) {
			System.out.println("It ain't your turn, maggot!");
			return false;
		}
		
		if(client != board.get(fromRow, fromCol)) {
			System.out.println("That's not your piece, bitch!");
			return false;
		}
		
		boolean jumped = jumpIsValid(fromRow, fromCol, toRow, toCol);

		if (jumped || moveIsValid(fromRow, fromCol, toRow, toCol)) { // if this is a legal move, then execute it
			
			SquarePlayer piece = board.get(fromRow, fromCol);
			boolean king = board.isKing(fromRow, fromCol);

			placePiece(toRow, toCol, piece); // 'Copy' the player to its new position
			if (king) {
				board.makeKing(toRow, toCol);
			}

			if (piece == SquarePlayer.PlayerOne && toRow == CheckerBoard.BOARD_SIZE-1) {// Check to see if the player is now a king
				board.makeKing(toRow, toCol);
				
			}
			else if (piece == SquarePlayer.PlayerTwo && toRow == 0) {
				board.makeKing(toRow, toCol);
				
			}
			// Remove the jumped player
			if (jumped)
				placePiece((fromRow+toRow)/2,(fromCol+toCol)/2, SquarePlayer.Empty);

			placePiece(fromRow, fromCol, SquarePlayer.Empty); // Remove player from old location
			
			turn = turn.swap();
			setChanged();
			notifyObservers();
			
			if(gameOver()) {
				System.out.println("That's all, folks");
			}
			return true;
		} else {
			return false;
		}

	}
	
	public void placePiece (int toRow, int toCol, SquarePlayer p) {
		board.set(toRow, toCol, p);
		
		Pieces piece = new Pieces (toRow, toCol, p);
			try {
				player1.getBasicRemote().sendObject(piece);
				player2.getBasicRemote().sendObject(piece);
				System.out.println("Gonna try to send something");
			} catch (IOException | EncodeException e) {
				System.err.println("Problem with sending a piece.");
				throw new RuntimeException(e);
				}
		
	}


	
	/**
	 *	Determines if there is a winner.
	Count the number of pieces left for each player.  If there are not any
	pieces left for a player, they lose, and the other player is the winner.
	@return returns true if there is a winner - all of one color's pieces are gone
	 */
	public boolean gameOver()
	{
		int playerOneScore = 0, playerTwoScore = 0;
		for (int r = 0; r < CheckerBoard.BOARD_SIZE; r++)
			for (int c = 0; c < CheckerBoard.BOARD_SIZE; c++)
				if (board.get(r, c) == SquarePlayer.PlayerOne)
					playerOneScore++;
				else if (board.get(r, c) == SquarePlayer.PlayerTwo)
					playerTwoScore++;
		return ((playerOneScore == 0) || (playerTwoScore == 0));
	}

	// DEBUG method
	public String toString()
	{
		String b = "";
		for (int r = 0; r < CheckerBoard.BOARD_SIZE; r++)
		{
			for (int c = 0; c < CheckerBoard.BOARD_SIZE-1; c++)
				b += board.get(r, c) + "\t";
			b += board.get(r, CheckerBoard.BOARD_SIZE-1) + "\n";
		}
		return b;
	}

	public boolean canMoveFrom(SquarePlayer currentPlayer, int fromRow, int fromCol) {
		return (board.get(fromRow, fromCol) == currentPlayer);

	}
	
	public String getPlayer1() {
		return player1.getId();
	}
	
	public String getPlayer2() {
		return player2.getId();
	}
	
	public Session getSession1() {
		return player1;
	}
	
	public Session getSession2() {
		return player2;
	}
}
