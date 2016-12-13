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
		Server server = new Server("localhost", 9000, "/checkers", null, CheckersServerEndpoint.class);
		try {
			server.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			Scanner keyboard = new Scanner(System.in);
			System.out.print("Press a key to stop me.");
			String message = reader.readLine();
			System.out.println(message);
			keyboard.next();	
			keyboard.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			
			server.stop();
		}
		
		
	}

}
