package checkers.client;

import checkers.common.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.websocket.*;

public class CheckersChat extends JPanel implements ActionListener{
	private JTextArea chat;
	private static JTextField entry;
	private static JButton send;
	Session player;
	
	public CheckersChat(Session player) {
		this.player = player;
		//add textarea and textfield
		//add button that sends string textfield to server
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JTextArea chat = new JTextArea(20, 40);
		JScrollPane scroller = new JScrollPane(chat);
		this.add(scroller);
		add(chat);
		
		JTextField entry = new JTextField(40);
		add(entry);
		
		JButton send = new JButton("Send");
		send.addActionListener(this);
		add(send);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(entry.getText());
		chatMessage msg = new chatMessage(entry.getText());
		try {
			player.getBasicRemote().sendObject(msg);
		} catch (IOException | EncodeException y) {
			System.err.println("Problem with sending a message.");
		}
	}
	
	public void received(String msg) {
		chat.append(msg + "/n");
	}
}
