package checkers.client;

import checkers.common.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.websocket.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.glassfish.tyrus.client.ClientManager;

public class CheckersClient extends JFrame implements MouseListener {

	

	private CheckerboardCanvas cbCanvas; // the 'view' of the checkerboard
	private CheckerBoard board;			// the client's part of the 'model'
	private CheckersChat chatter;

	private int canvasTopInset;			// distance Canvas is placed from the top

	private int fromRow, fromCol;		// Where the checker is moving from

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private SquarePlayer currentPlayer;	
	
	private int id;
	
	public Session player;
	
	
	public CheckersClient(Session player) {

		super("NetCheckers");
		setSize(900,450); //expand this
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);        
		id = (int) Math.round(Math.random() * 100000);
		this.player = player;
		
		board = new CheckerBoard();			// Create the 'model'model
	
		cbCanvas = new CheckerboardCanvas(board);

		add(cbCanvas);					// Add the view to this frame
		chatter = new CheckersChat(player);
		add(BorderLayout.EAST, chatter);
	
		addMouseListener(this);				// Have this program listen for mouse events

		setVisible(true);
		canvasTopInset = getInsets().top;	// may be needed to get accurate mouse-click location

	}
	
	public void gotPiece (int row, int col, SquarePlayer p) {
		board.set(row, col, p);
		cbCanvas.repaint();
	}
	
	public void gotMessage(String msg) {
		chatter.received(msg);
	}

	/**
	 * Gets the pixel location of the mouse click and turns it into the
	 * row and column in the grid of the checker location.
	 * 
	 * @param e the mouse event that just occurred
	 */
	public void mousePressed(MouseEvent e)
	{
		fromRow = cbCanvas.getRow(e.getY()-canvasTopInset);
		fromCol = cbCanvas.getCol(e.getX());

	//	for debugging
	//	System.err.println("Mouse down: " + fromRow + "-" + fromCol);
	}

	/**
	 * 
	 * Attempts to move piece from mouse-press location to mouse-release location. 

	 * @param e the mouse event that just occurred
	 */

	public void mouseReleased(MouseEvent e) {

		int toRow = cbCanvas.getRow(e.getY()-canvasTopInset); // adjust coordinates to account for the checkerboard's location
		int toCol = cbCanvas.getCol(e.getX());
		Play move = new Play(fromRow, fromCol, toRow, toCol);
		// TODO: what happens here?
		try {
			System.out.println(player.getBasicRemote());
			player.getBasicRemote().sendObject(move);
		} catch (IOException | EncodeException e1) {
			e1.printStackTrace();
		}
	}


	// not used
	public void mouseDragged(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}


}