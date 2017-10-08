package checkers.client;

import checkers.common.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.websocket.*;
import org.glassfish.tyrus.client.ClientManager;


public class CheckersChat extends JPanel implements ActionListener{
	private JTextArea chat = new JTextArea(20, 40);
	private JTextField entry = new JTextField(40);
	private JButton send = new JButton("Send");
	public Session player;
	
	public CheckersChat(Session player) {
		this.player = player;
		//add textarea and textfield
		//add button that sends string textfield to server
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JScrollPane scroller = new JScrollPane(chat);
		this.add(scroller);
		add(chat);
		
		add(entry);
		System.out.println("**********");
		System.out.println(send);
		
		send.addActionListener(this);
		add(send);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");
		System.out.println(entry.getText());
		chatMessage msg = new chatMessage(entry.getText());
		System.out.println(msg);
		try {
			System.out.println(player.getBasicRemote());
			player.getBasicRemote().sendObject(msg);
		} catch (IOException | EncodeException y) {
			System.err.println("Problem with sending a message.");
		}
	}
	
	public void received(String msg) {
		String newline = "\n";
		chat.append(msg + newline);
	}
}
