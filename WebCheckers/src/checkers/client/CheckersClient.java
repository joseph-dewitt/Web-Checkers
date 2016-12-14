package checkers.client;

import checkers.common.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.glassfish.tyrus.client.ClientManager;

@ClientEndpoint (decoders = { PiecesDecoder.class }, encoders = {PlayEncoder.class })
public class CheckersClient extends JFrame implements MouseListener {

	@OnOpen
	public void onOpen(Session player) {
		logger.info("Connected ... " + player.getId());
		System.out.println("Yay");
	}
	
	@OnMessage
	public void onMessage (Session player, Pieces piece) {
		System.out.println("Gotta message here");
		board.set(piece.getRow(), piece.getCol(), SquarePlayer.valueOf(piece.getPlayer()), false);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
		latch.countDown();
	}
	
	private static CountDownLatch latch;

	private CheckerboardCanvas cbCanvas; // the 'view' of the checkerboard
	private CheckerBoard board;			// the client's part of the 'model'

	private int canvasTopInset;			// distance Canvas is placed from the top

	private int fromRow, fromCol;		// Where the checker is moving from

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private SquarePlayer currentPlayer;	
	
	private int id;
	
	
	public CheckersClient(Session player) {

		super("NetCheckers");
		setSize(450,450);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);        
		id = (int) Math.round(Math.random() * 100000);
		
		board = new CheckerBoard(player);			// Create the 'model'model
	
		cbCanvas = new CheckerboardCanvas(board);

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
						latch = new CountDownLatch(1);

						Session player;
						// WebSocketContainer container = ContainerProvider.getWebSocketContainer();
						ClientManager client = ClientManager.createClient();
						try {
							/*
							 * player = container.connectToServer(CheckersClient.class, new URI("ws://localhost:9000/checkers/play"));
							player.getBasicRemote().sendText("Check me out");
							*/
							client.connectToServer(CheckersClient.class, new URI("ws://localhost:9000/checkers/play"));
				            
//							System.out.println(player.toString());
//							CheckersClient game = new CheckersClient(player);
							latch.await();

							System.out.println("Made a game");

						} catch (DeploymentException | URISyntaxException | IOException | InterruptedException e) {
							throw new RuntimeException(e);
						}

					}
				});
	}
}