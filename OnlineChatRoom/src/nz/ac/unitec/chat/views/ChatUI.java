package nz.ac.unitec.chat.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nz.ac.unitec.chat.constants.MessageType;
import nz.ac.unitec.chat.controllers.ClientTool;
import nz.ac.unitec.chat.models.Message;

@SuppressWarnings("serial")
public class ChatUI extends JFrame{
	
	private TextArea content = null;
	private JTextField input = null;
	private JButton button = null;
	private JPanel panel = null;
	private ClientTool clientTool = null;
	private String friendName = null;
	
	public ChatUI(final String friendName, final ClientTool clientTool, final JLabel eventSource){
		this.clientTool = clientTool;
		this.friendName = friendName;
		bindConnectionTool(this);
		content = new TextArea();
		content.setEditable(false);
		input = new JTextField(30);
		button = new JButton("Send");
		panel = new JPanel();
		panel.add(input);
		panel.add(button);
		
		this.add(content, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		pack();
		
		//Bind listener to button
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		
		
		this.setTitle("You are talking with " + friendName);
		this.setSize(450, 200);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.content.setFont(new Font("Verdana", Font.BOLD, 12));
		this.content.setForeground(Color.black);
		this.setLocation(640, 330);
		this.setVisible(true);
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				//When closing, send notice to the other side and remove the entry in chatUIMap and reveiverHasMyChatUI
				clientTool.completeChat(friendName);
				if(clientTool.getChatUIMap().get(friendName) != null){
					clientTool.getChatUIMap().remove(friendName);
				}
				if(clientTool.getReveiverHasMyChatUI().get(friendName) != null){
					clientTool.getReveiverHasMyChatUI().remove(friendName);
				}
				clientTool.addMouseListenerBackToLabel(eventSource);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
		
	}
	
	
	private void bindConnectionTool(ChatUI chatUI){
		this.clientTool.bindChatUI(friendName,chatUI);
	}
	
	private void sendMsg(){
		String text = input.getText();
		if(text != null && text != "" && text.length() != 0){
			clientTool.sendMsg(new Message(MessageType.REGULAR_PRIVATE, text, clientTool.getUsername(), this.friendName, new Date().toString()));
			content.append("You @ "+ new Date().toString() +": \n" + text + "\n\n");
			input.setText("");
		}else{
			JOptionPane.showMessageDialog(null, "Please enter");
		}
	}

	public TextArea getContent() {
		return content;
	}

	public void setContent(TextArea content) {
		this.content = content;
	}

	public JTextField getInput() {
		return input;
	}

	public void setInput(JTextField input) {
		this.input = input;
	}

	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public ClientTool getClientTool() {
		return clientTool;
	}

	public void setClientTool(ClientTool clientTool) {
		this.clientTool = clientTool;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	
}
