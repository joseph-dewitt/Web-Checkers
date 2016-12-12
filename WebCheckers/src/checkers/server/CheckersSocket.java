package checkers.server;

import java.io.*;
import java.util.*;

import org.glassfish.tyrus.server.*;

public class CheckersSocket {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		openVenue();
	}
	
	public static void openVenue() {
		Server server = new Server("localhost", 8025, "/checkers", null, CheckersServerEndpoint.class);
		try {
			server.start();
			Scanner keyboard = new Scanner(System.in);
			System.out.print("Press a key to stop me.");
			keyboard.next();	
			keyboard.close();
		} catch (Exception e) {
			System.out.println("Something broke");
		} finally {
			
			server.stop();
		}
		
		
	}

}
