package nz.ac.unitec.chat.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import nz.ac.unitec.chat.controllers.ClientTool;

@SuppressWarnings("serial")
public class FriendListUI extends JFrame{
	
	private JScrollPane scrollPane = null;
	private ClientTool clientTool = null;
	private JPanel friendListPanel = null;
	private JPanel broadcastPanel = null;
	private JTextField broadcastInputField = null;
	private JButton sendBroadcast = null;
	private TextArea broadcastContent = null;
	private FriendListUI _this = null;
	
	public FriendListUI(final ClientTool clientTool){
		this.clientTool = clientTool;
		
		this.friendListPanel = new JPanel();
		friendListPanel.setPreferredSize(new Dimension(220, 300));
		friendListPanel.setLayout(new BorderLayout());
		
		JPanel panel = clientTool.initFriendListPanel();
		scrollPane = new JScrollPane(panel);
		friendListPanel.add(scrollPane, BorderLayout.CENTER);
		
		broadcastPanel = new JPanel();
		broadcastPanel.setLayout(new BorderLayout());
		broadcastPanel.setPreferredSize(new Dimension(460, 300));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setPreferredSize(new Dimension(450, 35));
		this.broadcastInputField = new JTextField(30); 
		inputPanel.add(broadcastInputField);
		
		//Bind enter
		broadcastInputField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				broadcast();
			}
		});
		
		this.sendBroadcast = new JButton("Broadcast");
		
		//Bind click
		sendBroadcast.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				broadcast();
			}
		});
		
		inputPanel.add(sendBroadcast);
		
		this.broadcastContent = new TextArea();
		broadcastContent.setEditable(false);
		
		this.broadcastContent.setFont(new Font("Verdana", Font.BOLD, 12));
		this.broadcastContent.setForeground(Color.black);
		
		broadcastPanel.add(broadcastContent, BorderLayout.CENTER);
		broadcastPanel.add(inputPanel, BorderLayout.SOUTH);
		
		this.add(friendListPanel, BorderLayout.WEST);
		this.add(broadcastPanel, BorderLayout.EAST);
		this.setSize(680, 300);
		this.setResizable(false);
		this.setTitle("Online friend list");
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
				if(clientTool.isServerShutdownConfirmation()){
					System.exit(0);
					_this.setDefaultCloseOperation(EXIT_ON_CLOSE);
				}else{
					clientTool.closeClient();
					System.exit(0);
					_this.setDefaultCloseOperation(EXIT_ON_CLOSE);
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
		this.clientTool.bindFriendUI(this);
		this.setLocation(665, 330);
		_this = this;
		this.setVisible(true);
	}
	
	private void broadcast(){
		String text = this.broadcastInputField.getText();
		if(text != null && text != "" && text.length() != 0){
			this.clientTool.broadcast(text);
			this.broadcastInputField.setText("");
		}
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	
	public TextArea getBroadcastContent() {
		return broadcastContent;
	}

	public ClientTool getClientTool() {
		return clientTool;
	}

	public void setClientTool(ClientTool clientTool) {
		this.clientTool = clientTool;
	}

	public JPanel getFriendListPanel() {
		return friendListPanel;
	}

	public void setFriendListPanel(JPanel friendListPanel) {
		this.friendListPanel = friendListPanel;
	}

	public JPanel getBroadcastPanel() {
		return broadcastPanel;
	}

	public void setBroadcastPanel(JPanel broadcastPanel) {
		this.broadcastPanel = broadcastPanel;
	}

	public JTextField getBroadcastInputField() {
		return broadcastInputField;
	}

	public void setBroadcastInputField(JTextField broadcastInputField) {
		this.broadcastInputField = broadcastInputField;
	}

	public JButton getSendBroadcast() {
		return sendBroadcast;
	}

	public void setSendBroadcast(JButton sendBroadcast) {
		this.sendBroadcast = sendBroadcast;
	}

	public void setBroadcastContent(TextArea broadcastContent) {
		this.broadcastContent = broadcastContent;
	}
}
