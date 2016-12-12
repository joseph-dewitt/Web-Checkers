package checkers.client;

import checkers.common.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;
import org.glassfish.tyrus.client.ClientManager;



public class CheckersClient extends JFrame implements MouseListener {

	private CheckerboardCanvas cbCanvas; // the 'view' of the checkerboard
	private CheckerBoard board;			// the client's part of the 'model'

	private int canvasTopInset;			// distance Canvas is placed from the top

	private int fromRow, fromCol;		// Where the checker is moving from

	private SquarePlayer currentPlayer;	
	
	private int id;
	public CheckersClient(Session player) {

		super("NetCheckers");
		setSize(450,450);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);        
		id = (int) Math.round(Math.random() * 100000);
		board = new CheckerBoard();			// Create the 'model'model
		
		cbCanvas = new CheckerboardCanvas();

		add(cbCanvas);					// Add the view to this frame
	
		addMouseListener(this);				// Have this program listen for mouse events

		setVisible(true);
		canvasTopInset = getInsets().top;	// may be needed to get accurate mouse-click location

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
	}


	// not used
	public void mouseDragged(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}


	public static void main(String[] args) {

		EventQueue.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						
						// TODO: does any other setup need to happen?
						
						Session player;
						ClientManager client = ClientManager.createClient();
						try {
							player = client.connectToServer(CheckersClient.class, new URI(
									"ws://localhost:8025/websockets/play"));
							CheckersClient game = new CheckersClient(player);

						} catch (DeploymentException | URISyntaxException | IOException e) {
							throw new RuntimeException(e);
						}

					}
				});
	}
}