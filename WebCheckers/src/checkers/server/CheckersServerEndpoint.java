package checkers.server;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import checkers.common.*;


@ServerEndpoint(value = "/play",decoders = { PlayDecoder.class }, encoders = {
		PiecesEncoder.class, chatMessageEncoder.class})
public class CheckersServerEndpoint {
	
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private static ArrayList<CheckersModel> model = new ArrayList<CheckersModel>();
    private static boolean waiting = false;
	private static Session waitingPlayer;
        
    @OnOpen
    public void onOpen(Session player) throws EncodeException {
    	logger.info("Look out! Here comes " + player.getId());
    	if (waiting) {
    		clientPairer(waitingPlayer, player);
    	}
    	else {
    		waitingPlayer = player;
    		waiting = true;
            contactClient(player, "You must wait for an opponent.");
    		System.out.println("He's gonna have to wait for an opponent");
    	}
    }

    @OnMessage
    public void onMessage(Session player, Message msg) throws EncodeException {
    	System.out.println("onMessage");
        for (int i = 0; i < model.size(); i++) {
    		if (player.getId().equals(model.get(i).getPlayer1())) {
    			if (msg instanceof Play) {
    				Play play = (Play) msg;
    				model.get(i).move(	play.getfromRow(),
    									play.getfromCol(),
    									play.gettoRow(),
    									play.gettoCol(),
    									SquarePlayer.valueOf("PlayerOne"));
    			}
    			if (msg instanceof chatMessage) {
    				try {
    	                model.get(i).getSession1().getBasicRemote().sendObject((chatMessage) msg);
    	                model.get(i).getSession2().getBasicRemote().sendObject((chatMessage) msg);    	                
    	            } catch (IOException ex) {
    	                Logger.getLogger(CheckersServerEndpoint.class.getName()).log(Level.SEVERE, null, ex);
    	            }
    			}
    		}
    		if (player.getId().equals(model.get(i).getPlayer2())) {
    			if (msg instanceof Play) {
    				Play play = (Play) msg;
    				model.get(i).move(	play.getfromRow(), 
    								    play.getfromCol(), 
    								    play.gettoRow(), 
    								    play.gettoCol(), 
    								    SquarePlayer.valueOf("PlayerTwo"));
    			}
    			if (msg instanceof chatMessage) {
    				try {
                        model.get(i).getSession1().getBasicRemote().sendObject((chatMessage) msg);
    	                model.get(i).getSession2().getBasicRemote().sendObject((chatMessage) msg);    	                
    	            } catch (IOException ex) {
    	                Logger.getLogger(CheckersServerEndpoint.class.getName()).log(Level.SEVERE, null, ex);
    	            }
    			}
    		}
    	}
    }
    
    @OnClose
	public void onClose(Session player, CloseReason closeReason) throws EncodeException {
		int emptyGame = 0;
        Session remaining = null;
        if (player == waitingPlayer) {
            waiting = false;
            waitingPlayer = null;
            return;
        }
        logger.info(String.format("Session %s closed because of %s",
				player.getId(), closeReason));
        for (int i = 0; i < model.size(); i++) {
            if (player.getId().equals(model.get(i).getPlayer1())) {
                System.out.println("Player One left");
                remaining = model.get(i).getSession2();
                emptyGame = i;
            }
            if (player.getId().equals(model.get(i).getPlayer2())) {
                System.out.println("Player Two left");
                remaining = model.get(i).getSession1();
                emptyGame = i;
            }
            break;
        }
        model.remove(emptyGame);
        contactClient(remaining, "Your opponent left, we'll try to pair you with another.");
        cleanBoard(remaining);
        System.out.println(waiting);
        if (waiting) {
            waiting = false;
            clientPairer(waitingPlayer, remaining);
        } else {
            System.out.println(waiting);
            waitingPlayer = remaining;
            waiting = true;
            contactClient(waitingPlayer, "You must wait for an opponent.");
        }
        if (waiting) {
            System.out.println("There is now a player waiting");
        }
    }
    
    public void clientPairer (Session waitingPlayer, Session player) throws EncodeException {
    	CheckersModel e = new CheckersModel(waitingPlayer, player);
    	model.add(e);
        waiting = false;
        contactClient(waitingPlayer, "Thanks for waiting! You're green, that means you go first!");
        contactClient(player, "Greetings! You're white, but green goes first.");
        String greeting = "Have fun playing!";
        contactClient(waitingPlayer, greeting);
        contactClient(player, greeting);

    }

    public void contactClient (Session player, String msg) throws EncodeException {
        try {
            player.getBasicRemote().sendObject(new chatMessage(msg));                      
        } catch (IOException ex) {
                Logger.getLogger(CheckersServerEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cleanBoard (Session player) {
        for (int r = 0; r < CheckerBoard.BOARD_SIZE; r++) {
            for (int c = 0; c < CheckerBoard.BOARD_SIZE; c++) {
                try {
                    player.getBasicRemote().sendObject(new Pieces (r, c, SquarePlayer.Empty));
                } catch (IOException | EncodeException e) {
                    System.err.println("Message sending failed");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
