package nz.ac.unitec.zhiming.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nz.ac.unitec.zhiming.constant.MessageReceiver;
import nz.ac.unitec.zhiming.constant.MessageType;
import nz.ac.unitec.zhiming.model.Message;
import nz.ac.unitec.zhiming.model.ShapeEntity;
import nz.ac.unitec.zhiming.model.User;
import nz.ac.unitec.zhiming.view.CanvasUI;

public class ClientTool {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private User myself;
	private boolean msgReceiveThreadconnected;
	private CanvasUI canvasUI;
	private User server;
	private boolean isServerRunning;

	public ClientTool(){
		
	}
	
	public ClientTool(String username){
		this();
		User user = new User(username);
		myself = user;
		server = new User(MessageReceiver.SERVER);
	}
	
	public boolean connect() {
		boolean flag = false;
		try {
			this.socket = new Socket("localhost", 9090);
			if (this.socket != null) {
				oos = new ObjectOutputStream(socket.getOutputStream());
				msgReceiveThreadconnected = true;
				isServerRunning = true;
				new Thread(new receiveMsgThread()).start();
				Message msg = new Message(MessageType.SIGN_IN,
						MessageType.SIGN_IN, myself, new User(
								MessageReceiver.SERVER), new Date().toString());
				oos.writeObject(msg);
				oos.flush();
				flag = true;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public void synchronizeShape(HashSet<ShapeEntity> drawnShapes) {
		HashSet<ShapeEntity> hs = new HashSet<ShapeEntity>();
		hs.addAll(drawnShapes);
		Message msg = new Message(MessageType.SHAPE, MessageType.SHAPE,
				hs, myself, server, null);
		sendMsg(msg);
	}
	
	
	public void broadcast(String preMsg){
		Message msg = new Message(MessageType.BROADCAST, preMsg, myself, 
				new User(MessageReceiver.ALL), new Date().toString());
		sendMsg(msg);
	}
	
	private void sendMsg(Message msg) {
		try {
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			closeClient();
		}
	}
	
	public void closeClient(){
		try {
			if(isServerRunning){
				oos.writeObject(new Message(MessageType.CLIENT_CLOSE, MessageType.CLIENT_CLOSE, 
						myself, server, new Date().toString()));
				if(!msgReceiveThreadconnected){
					disconnect();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void disconnect(){
		try{
			msgReceiveThreadconnected = false;
			
			if(oos != null){
				oos.close();
			}
			if(ois != null){
				ois.close();
			}
			if(socket != null){
				socket.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void bindCanvasUI(CanvasUI canvasUI){
		this.canvasUI = canvasUI;
	}
	
	private void showBroadcastMsg(Message msg){
		TextArea broadcastContent = this.canvasUI.getContent();
		broadcastContent.append("  " + msg.getSender().getUsername() + " @ " + msg.getTime() + " says: \n" + "");
		broadcastContent.append("  " + msg.getContent() + "\n\n" + "");
	}
	
	private JPanel getFriendLabel(String[] friends, int friendAmount){
		JPanel panel = new JPanel(new GridLayout(15, 1));
		for (int i = 0; i < friends.length; i++) {
			JLabel label = new JLabel(friends[i] + "");
			panel.add(label);
		}
		return panel;
	}
	
	private void updatedFriendList(Message msg) {
		String[] tempFriends = msg.getContent().split("\\|");
		JPanel friendPanel = getFriendLabel(tempFriends,tempFriends.length);
		if (this.canvasUI != null) {
			// ScrollPane is the only element in the panel
			this.canvasUI.getPnlRight().removeAll();
			JScrollPane newScrollPane = new JScrollPane(friendPanel);
			newScrollPane.setPreferredSize(new Dimension(150, 300));
			this.canvasUI.getPnlRight().add(newScrollPane,BorderLayout.CENTER);
			// Changn the reference to the new scroll pane
//			this.canvasUI.setScrollPane(newScrollPane);
			this.canvasUI.getPnlRight().revalidate();
			this.canvasUI.repaint();
		}
	}

	private class receiveMsgThread implements Runnable {
		@Override
		public void run() {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				while (msgReceiveThreadconnected) {
					Message msg = (Message) ois.readObject();
					if (msg == null) {
						msgReceiveThreadconnected = false;
					}else if (msg.getType().equalsIgnoreCase(MessageType.BROADCAST)) {
						// Broadcast to all, show in broadcast UI
						showBroadcastMsg(msg);
					} else if (msg.getType().equalsIgnoreCase(MessageType.CLIENT_CLOSE)) {
						msgReceiveThreadconnected = false;
					} else if (msg.getType().equalsIgnoreCase(MessageType.FRIEND_LIST_UPDATE)) {
						// Update friend list
						updatedFriendList(msg);
					} else if (msg.getType().equalsIgnoreCase(MessageType.SERVER_SHUTDOWN)) {
						// Notice user and close all open windows
						// Send confirmation back to server and disconnect
						// server, close all resources
						msgReceiveThreadconnected = false;
						isServerRunning = false;
						serverShutdown(msg);
					} else if(msg.getType().equalsIgnoreCase(MessageType.SHAPE)){
						//»­Í¼ÐÎ
						//draw figures
						HashSet<ShapeEntity> drawnShapes = msg.getDrawnShapes();
						if(drawnShapes != null){
							canvasUI.paintDrawnShapes(drawnShapes);
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void serverShutdown(Message msgFromServer){
		try {
			//Send confirmation back to server
			Message msg = new Message(MessageType.SERVER_SHUTDOWN_RECEIVED_CONFIRMATION, 
					MessageType.SERVER_SHUTDOWN_RECEIVED_CONFIRMATION, myself, server, new Date().toString());
			oos.writeObject(msg);
			//Show notice on broadcast panel
			showBroadcastMsg(new Message(MessageType.SERVER_SHUTDOWN, 
					"The server is shutting down, all active dialog windows are disable now", 
					server, null, new Date().toString()));
			//Disable all open windows
			this.canvasUI.getBtnSend().setEnabled(false);
			this.canvasUI.getPnlLeft().setEnabled(false);
			this.canvasUI.getPnlCenter().setEnabled(false);
			this.canvasUI.getPnlRight().setEnabled(false);
			//Closes all resources
			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public User getMyself() {
		return myself;
	};
}