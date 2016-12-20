package checkers.server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import checkers.common.*;


@ServerEndpoint(value = "/play",decoders = { PlayDecoder.class }, encoders = {
		PiecesEncoder.class})
public class CheckersServerEndpoint {
	
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static ArrayList<CheckersModel> model = new ArrayList<CheckersModel>();
    private static boolean waiting = false;
	private static Session waitingPlayer;
        
    @OnOpen
    public void onOpen(Session player) {
    	logger.info("Look out! Here comes " + player.getId());
    	if (waiting) {
    		waiting = false;
    		clientPairer(waitingPlayer, player);
    	}
    	else {
    		waitingPlayer = player;
    		waiting = true;
    		System.out.println("He's gonna wait");
    	}
    }

    @OnMessage
    public void onMessage(Session player, Play play) throws EncodeException {
    	for (int i = 0; i < model.size(); i++) {
    		if (player.getId().equals(model.get(i).getPlayer1())) {
    			model.get(i).move(	play.getfromRow(),
    								play.getfromCol(),
    								play.gettoRow(),
    								play.gettoCol(),
    								SquarePlayer.valueOf("PlayerOne"));
    		}
    		if (player.getId().equals(model.get(i).getPlayer2())) {
    			model.get(i).move(	play.getfromRow(), 
    								play.getfromCol(), 
    								play.gettoRow(), 
    								play.gettoCol(), 
    								SquarePlayer.valueOf("PlayerTwo"));
    		}
    	}
    	
    }
    
    @OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("2Session %s closed because of %s",
				session.getId(), closeReason));
    }
    
    public void clientPairer (Session waiting, Session player) {
    	CheckersModel e = new CheckersModel(waiting, player);
    	model.add(e);
    }
}
