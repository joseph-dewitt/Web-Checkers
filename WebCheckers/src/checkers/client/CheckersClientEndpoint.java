package checkers.client;

import org.glassfish.tyrus.client.ClientManager;

import checkers.common.*;
import checkers.common.*;
import javax.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

@ClientEndpoint (decoders = { PiecesDecoder.class }, encoders = {PlayEncoder.class })

public class CheckersClientEndpoint {

	@OnOpen
	public void onOpen(Session player) {
		logger.info("Connected ... " + player.getId());
		try {
			player.getBasicRemote().sendText("Are you still there?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Yay");
	}
	
	@OnMessage
	public void onMessage (Session player, Pieces piece) {
		System.out.println("Gotta message here");
		//board.set(piece.getRow(), piece.getCol(), SquarePlayer.valueOf(piece.getPlayer()), false);
	}
	
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
		latch.countDown();
	}
	
	private static CountDownLatch latch;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static CheckersClient checkersClient;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		latch = new CountDownLatch(1);
		
		Session player;
		ClientManager client = ClientManager.createClient();
		try {
			player = client.connectToServer(CheckersClientEndpoint.class, new URI("ws://localhost:9000/checkers/play"));
            player.getBasicRemote().sendText("Hi I'm Joe.");
            checkersClient = new CheckersClient(player);
			latch.await();

			System.out.println("Made a game");

		} catch (DeploymentException | URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

}
