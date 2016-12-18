package checkers.server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import checkers.common.*;


@ServerEndpoint(value = "/play",decoders = { PlayDecoder.class }, encoders = {
		PiecesEncoder.class})
public class CheckersServerEndpoint {
	
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static CheckersModel model;
    private static boolean waiting = false;
	private static Session waitingPlayer;
        
    @OnOpen
    public void onOpen(Session player) {
    	logger.info("Look out! Here comes " + player.getId());
    	if (waiting) {
    		waiting = false;
    		clientPairer(player);
    	}
    	else {
    		waitingPlayer = player;
    		waiting = true;
    	}
    }

    @OnMessage
    public void onMessage(Session player, Play play) throws EncodeException {
    	
    	boolean biscuit = model.move(play.getfromRow(), play.getfromCol(), play.gettoRow(), play.gettoCol());
    }
    
    @OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("2Session %s closed because of %s",
				session.getId(), closeReason));
    			model = null;
    }
    
    public void clientPairer (Session player) {
    	model = new CheckersModel(waitingPlayer, player);
    }
}
