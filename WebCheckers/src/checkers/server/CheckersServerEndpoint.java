package checkers.server;

import java.io.IOException;
import java.util.logging.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import checkers.common.*;


@ServerEndpoint(value = "/play",decoders = { PlayDecoder.class }, encoders = {
		PiecesEncoder.class})
public class CheckersServerEndpoint {
	
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private CheckersModel model;
    
    @OnOpen
    public void onOpen(Session player) {
    	logger.info("Look out! Here comes " + player.getId());
    	if (model == null) {
    		model = new CheckersModel(player);
    	}
    	
    	System.out.println("Here comes dat boi");
    	
    }
/*
    @OnMessage
    public void onMessage(Session player, Play play) throws EncodeException {
    	
    }*/
    @OnMessage
    public void textMessage(Session player, String message) {
    	System.out.println(message);
    }
    
    @OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("2Session %s closed because of %s",
				session.getId(), closeReason));
	}
}
