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

@ClientEndpoint (decoders = { PlayDecoder.class }, encoders = {PlayEncoder.class, chatMessageEncoder.class })

public class CheckersClientEndpoint {

	@OnOpen
	public void onOpen(Session player) {
		logger.info("Connected ... " + player.getId());
	/*	try {
			player.getBasicRemote().sendText("Are you still there?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        checkersClient = new CheckersClient(player);

		System.out.println("Yay");
	}
	
	@OnMessage
	public void onMessage (Session player, Message msg) {
		if (msg instanceof Pieces) {
			Pieces piece = (Pieces) msg;
			checkersClient.gotPiece(piece.getRow(), piece.getCol(), SquarePlayer.valueOf(piece.getPlayer()));
		}
		if (msg instanceof chatMessage) {
			chatMessage stuff = (chatMessage) msg;
			checkersClient.gotMessage(stuff.get());
			System.out.println(stuff.get());
		}
	}
	
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s close because of %s",
				session.getId(), closeReason));
		latch.countDown();
	}
	
	private static CountDownLatch latch;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public static CheckersClient checkersClient;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		latch = new CountDownLatch(1);
		
		Session player;
		ClientManager client = ClientManager.createClient();
		try {
			player = client.connectToServer(CheckersClientEndpoint.class, new URI("ws://localhost:9000/checkers/play"));
            player.getBasicRemote().sendText("Hi I'm Joe.");
			latch.await();

			System.out.println("Made a game");

		} catch (DeploymentException | URISyntaxException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

}
