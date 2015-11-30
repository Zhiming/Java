package nz.ac.unitec.chat.controllers;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import nz.ac.unitec.chat.constants.FriendListConstants;
import nz.ac.unitec.chat.constants.GlobalConstants;
import nz.ac.unitec.chat.constants.MessageReceiver;
import nz.ac.unitec.chat.constants.MessageType;
import nz.ac.unitec.chat.listeners.MouseListenerForChat;
import nz.ac.unitec.chat.models.ConnectionPool;
import nz.ac.unitec.chat.models.Message;
import nz.ac.unitec.chat.models.User;
import nz.ac.unitec.chat.views.ChatUI;
import nz.ac.unitec.chat.views.FriendListUI;

public class ClientTool {
	private boolean serverShutdownConfirmation = false;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private Socket clientSocket = null;
	private FutureTask<Message> future = null;
	private String username = null;
	private receiveMsg recvMsgThread = null;
	private Map<String, ChatUI> chatUIMap = null;
	private FriendListUI friendListUI = null;
	//Three statuses in reveiverHasMyChatUI
	//True - the other side has a chatUI with me
	//False - either mine or the other side's ChatUI has been closed   -> When it is false, no need too send the dialog complete signal to the other side
	//null - the other side don't have my chatUI
	private Map<String, Boolean> reveiverHasMyChatUI = null;
	//Current online friends
	private Map<String, String> friendMap = null;
	//Current friend list panel
	private JPanel latestFriendListPanel = null;
	private boolean exitFlag = false;
	
//public static void main(String[] args) {
//	ClientTool c = new ClientTool("1");
//	c.loginValidate("1", "123");
//	c.sendMsg(new Message(MessageType.REGULAR_PRIVATE, "test", c.getUsername(), "2", new Date().toString()));
//}
	
	public ClientTool(String username, String serverIp){
		try{
			this.username = username;
			this.clientSocket = ConnectionPool.getConnection(username, serverIp);
			if(this.clientSocket != null){
				oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
				this.recvMsgThread = new receiveMsg();
				this.chatUIMap = new HashMap<String, ChatUI>();
				this.reveiverHasMyChatUI = new HashMap<String, Boolean>();
				this.friendMap = new HashMap<String, String>();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void bindChatUI(String receiver, ChatUI chatUIOnMySide){
		this.chatUIMap.put(receiver, chatUIOnMySide);
	}
	
	public void bindFriendUI(FriendListUI friendListUI){
		this.friendListUI = friendListUI;
	}
	
	private void disconnect(){
		try{
			ConnectionPool.removeConnection(this.username);
			//Might be not useful as the thread blocks at readObject(). 
			//Need to handle it before the following operation
			this.recvMsgThread.setMsgReceiveThreadconnected(false);
			if(oos != null){
				oos.close();
			}
			if(ois != null){
				ois.close();
			}
			if(clientSocket != null){
				clientSocket.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void initRecevierDialog(String receiver){
		Message msg = new Message(MessageType.INIT_DIALOG, "INIT_DIALOG", this.username, receiver, new Date().toString());
		sendInitMsg(msg);
	}
	
	public void completeChat(String receiver){
		if(reveiverHasMyChatUI.get(receiver) != null && reveiverHasMyChatUI.get(receiver) != false){
			Message msg = new Message(MessageType.DIALOG_CLOSE, "DIALOG_CLOSE", this.username, receiver, new Date().toString());
			sendMsg(msg);
		}
		
	}
	
	private void sendInitMsg(Message msg){
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}
	
	public void sendMsg(Message msg){
		msg.setSender(username);
		msg.setTime(new Date().toString());
		try {
			//Check whether msg receiver has a opened chatting UI. Otherwise init one for them
			if(this.reveiverHasMyChatUI.get(msg.getReceiver()) == null || this.reveiverHasMyChatUI.get(msg.getReceiver()) == false){
				initRecevierDialog(msg.getReceiver());
			}
			while(this.reveiverHasMyChatUI.get(msg.getReceiver()) == null || this.reveiverHasMyChatUI.get(msg.getReceiver()) == false){
				Thread.sleep(100);
			}
			oos.writeObject(msg);
		} 
		catch (IOException e) {
			e.printStackTrace();
			disconnect();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			disconnect();
		}
	}
	
	public void broadcast(String preMsg){
		Message msg = new Message(MessageType.BROADCAST, preMsg, username, MessageReceiver.ALL, new Date().toString());
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}
	
	public void closeClient(){
		try {
			//send closing to server
			oos.writeObject(new Message(MessageType.CLIENT_CLOSE, MessageType.CLIENT_CLOSE, this.username, MessageReceiver.SERVER, new Date().toString()));
			//Send null to server side so that the ObjectInputStream could read it and avoid EOF exception
//			oos.writeObject(null);
			while(this.recvMsgThread.isMsgReceiveThreadconnected()){
				//Wait until receive msg thread closes
				Thread.sleep(100);
			}
			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String userRegister(String name, String password){
		String registerResult = null;
		try{
			Message msg = new Message(MessageType.USER_REGISTRATION, password, name, MessageReceiver.SERVER, new Date().toString());
			oos.writeObject(msg);
			oos.flush();
			this.future = new FutureTask<Message>(new LoginOrRegisterThread());
			new Thread(future).start();
			while(!future.isDone()){
				Thread.sleep(100);
			}
			registerResult = this.future.get().getContent();
			disconnect();
		}
		catch (IOException e) {
			e.printStackTrace();
			disconnect();
		} 
		catch (ExecutionException e) {
			e.printStackTrace();
			disconnect();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			disconnect();
		}
		return registerResult;
	}
	
	public String loginValidate(String name, String password){
		String authorization = null;
		try {
			Message msg = new Message(MessageType.LOGIN_AUTHORIZATION, password, name, MessageReceiver.SERVER, new Date().toString());
			oos.writeObject(msg);
			oos.flush();
			this.future = new FutureTask<Message>(new LoginOrRegisterThread());
			new Thread(future).start();
			while(!future.isDone()){
				Thread.sleep(100);
			}
			if(future.get().getContent().equals(GlobalConstants.AUTHORIZATION_GRANTED)){
				authorization = future.get().getContent();
				this.recvMsgThread = new receiveMsg();
				this.recvMsgThread.setMsgReceiveThreadconnected(true);
				new Thread(this.recvMsgThread).start();
			}else{
				authorization = future.get().getContent();
//System.out.println("authorization: " + authorization);
				disconnect();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			disconnect();
		} 
		catch (ExecutionException e) {
			e.printStackTrace();
			disconnect();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
			disconnect();
		}
		return authorization;
	}	
	
	private class LoginOrRegisterThread implements Callable<Message>{
		private boolean runFlag = true;
		@Override
		public Message call(){
			Message msg = null;
			try {
				ois = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				disconnect();
			}
			while(runFlag && msg == null){
				if(!clientSocket.isClosed()){
					try {
						msg = (Message)ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						disconnect();
					} catch (IOException e) {
						e.printStackTrace();
						disconnect();
					}
				}
			}
			runFlag = false;
			return msg;
		}
	}
	
	private void showPersonalMsg(Message msg){
		String sender = msg.getSender();
		String time = msg.getTime();
		String content = msg.getContent();
		
		ChatUI chatUI = chatUIMap.get(sender);
		chatUI.getContent().append(sender + " @ " + time + " says: \n" + content + " \n\n");
	}
	
	public void addMouseListenerBackToLabel(JLabel label){
//System.out.println("friendMap.get(label.getText()) - " + friendMap.get(label.getText()));
		if(this.friendMap.get(label.getText()) != null){
			//Add mouse listener back to the label
			JScrollPane scrollPane = this.friendListUI.getScrollPane();
			JPanel friendListPanel = (JPanel)((JViewport)scrollPane.getComponent(0)).getView();
			for(int i = 0; i < friendListPanel.getComponentCount(); i++){
				JLabel targetLabel = (JLabel)friendListPanel.getComponent(i);
				if(targetLabel.getText().equals(label.getText())){
					targetLabel.addMouseListener(new MouseListenerForChat(this));
				}
			}
		}
	}
	
	private void openNewChatUI(Message msg){
		//open a dialog
		//add to pool
		//send dialog complete msg back to sender
		if(chatUIMap.get(msg.getSender()) == null){
			JScrollPane scrollPane = this.friendListUI.getScrollPane();
			JPanel friendListPanel = (JPanel)((JViewport)scrollPane.getComponent(0)).getView();
			JLabel targetLabel = null;
			for(int i = 0; i < friendListPanel.getComponentCount(); i++){
				JLabel label = (JLabel)friendListPanel.getComponent(i);
				if(label.getText().equals(msg.getSender())){
					targetLabel = label;
					MouseListenerForChat mlc = (MouseListenerForChat)targetLabel.getMouseListeners()[0];
					targetLabel.removeMouseListener(mlc);
				}
			}
			ChatUI newChatUIOnMySide = new ChatUI(msg.getSender(), this, targetLabel);
			this.chatUIMap.put(msg.getSender(), newChatUIOnMySide);
		}
		this.reveiverHasMyChatUI.put(msg.getSender(), true);
		Message initResponse = new Message(MessageType.INIT_COMPLETE, "INIT_COMPLETE", msg.getReceiver(), msg.getSender(), new Date().toString());
		sendMsg(initResponse);
	}
	
	private void closeChatWhenSenderClose(Message msg){
		//Flag the status so that dialog complete signal won't be sent back
		reveiverHasMyChatUI.put(msg.getSender(), false);
		ChatUI currentChatUI = chatUIMap.get(msg.getSender());
		currentChatUI.getContent().append("System message: " + msg.getSender() + " has left...\n\n");
	}
	
	private void closeChatWhenSenderExitSystem(Message msg){
		reveiverHasMyChatUI.remove(msg.getSender());
		final ChatUI currentChatUI = chatUIMap.get(msg.getSender());
		chatUIMap.remove(msg.getSender());
		currentChatUI.getContent().append("System message: " + msg.getSender() + " exited system, window closing in 5 seconds");
		currentChatUI.getButton().setEnabled(false);
		currentChatUI.getInput().setEnabled(false);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				currentChatUI.dispose();
			}
		}, 5 * GlobalConstants.ONE_SECOND);
	}
	
	public JPanel initFriendListPanel(){
		while(this.latestFriendListPanel == null){
			try {
//System.out.println("wait for friend list");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//System.out.println("get friend list");
		return this.latestFriendListPanel;
	}
	
	private void updatedFriendList(Message msg){
		String[] tempFriends = msg.getContent().split("\\|");
//		if(this.friendListUI != null){
//			this.friendListUI.dispose();
//		}
//		new FriendListUI(this, tempFriends);
		if(tempFriends.length < FriendListConstants.DEFAULT_AMOUNT_OF_FRIEND_GRID_COLUMN){
			this.latestFriendListPanel = getFriendLabel(tempFriends, FriendListConstants.DEFAULT_AMOUNT_OF_FRIEND_GRID_COLUMN);
		}else{
			this.latestFriendListPanel = getFriendLabel(tempFriends, tempFriends.length);
		}
		//When a user logs in, the friend list UI is not constructed yet. Need to call initFriendListPanel() to implement the construction
		if(this.friendListUI != null){
			//ScrollPane is the only element in the panel
			this.friendListUI.getFriendListPanel().removeAll();
			JScrollPane newScrollPane = new JScrollPane(this.latestFriendListPanel);
			this.friendListUI.getFriendListPanel().add(newScrollPane, BorderLayout.CENTER);
			//Changn the reference to the new scroll pane
			this.friendListUI.setScrollPane(newScrollPane);
			this.friendListUI.getFriendListPanel().revalidate();
			this.friendListUI.repaint();
		}
	}
	
	private JPanel getFriendLabel(String[] friends, int friendAmount){
		JPanel panel = new JPanel(new GridLayout(friendAmount, 1, 4, 4));
		friendMap = new HashMap<String, String>();
		for(int i = 0; i < friends.length; i++){
			if(!(friends[i].equals(this.username))){
				//The reason why need this map is that when closing a dialog window, we need to check whether the other side is still online.
				//Otherwise, the mouse listener doesn't need to add to that.
				friendMap.put(friends[i], "PLACE_HOLDER");
				JLabel label = new JLabel(friends[i] + "");
				//Only add listener to those without open dialog window
				if(chatUIMap.get(friends[i]) == null){
					label.addMouseListener(new MouseListenerForChat(this));
				}
				panel.add(label);
			}
		}
		return panel;
	}
	
	private void showBroadcastMsg(Message msg){
		TextArea broadcastContent = this.friendListUI.getBroadcastContent();
		broadcastContent.append("  " + msg.getSender() + " @ " + msg.getTime() + " says: \n" + "");
		broadcastContent.append("  " + msg.getContent() + "\n\n" + "");
	}
	
	private void disableBroadcastPanel(){
		this.friendListUI.getBroadcastInputField().setEnabled(false);
		this.friendListUI.getSendBroadcast().setEnabled(false);
	}
	
	private void clearFriendList(){
		if(this.friendListUI != null){
			this.friendListUI.getFriendListPanel().removeAll();
			JScrollPane newScrollPane = new JScrollPane();
			this.friendListUI.getFriendListPanel().add(newScrollPane, BorderLayout.CENTER);
			this.friendListUI.setScrollPane(newScrollPane);
			this.friendListUI.getFriendListPanel().revalidate();
			this.friendListUI.repaint();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					friendListUI.dispose();
					exitFlag = true;
				}
			}, 3 * GlobalConstants.ONE_SECOND);
		}
	}
	
	private void disableAllOpenChatWindows(){
		Iterator<Entry<String, ChatUI>> it = chatUIMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, ChatUI> entry = it.next();
			final ChatUI activeChatUI = entry.getValue();
			activeChatUI.getContent().append("System message: server is shutting down, all active dialog windows will close in 3 seconds");
			activeChatUI.getButton().setEnabled(false);
			activeChatUI.getInput().setEnabled(false);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					activeChatUI.dispose();
				}
			}, 3 * GlobalConstants.ONE_SECOND);
		}
	}
	
	private void serverShutdown(Message msgFromServer){
		try {
			//Send confirmation back to server
			Message msg = new Message(MessageType.SERVER_SHUTDOWN_RECEIVED_CONFIRMATION, MessageType.SERVER_SHUTDOWN_RECEIVED_CONFIRMATION, this.username, MessageReceiver.SERVER, new Date().toString());
			oos.writeObject(msg);
			//Show notice on broadcast panel
			showBroadcastMsg(new Message(MessageType.SERVER_SHUTDOWN, "The server is shutting down, all active dialog windows will close in 3 seconds", MessageReceiver.SERVER, null, new Date().toString()));
			//Disable all open windows
			disableBroadcastPanel();
			disableAllOpenChatWindows();
			clearFriendList();
			//Closes all resources
			disconnect();
			while(!exitFlag){
				Thread.sleep(100);
			}
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	//Activate after logging in
	private class receiveMsg implements Runnable{
		private boolean msgReceiveThreadconnected = false;
		@Override
		public void run() {
			try {
				while(msgReceiveThreadconnected){
					Message msg = (Message)ois.readObject();
					if(msg == null){
						msgReceiveThreadconnected = false;
					}else if(msg.getType().equalsIgnoreCase(MessageType.REGULAR_PRIVATE)){
						//Forward to personal, show in personal chat UI
						showPersonalMsg(msg);
					}else if(msg.getType().equalsIgnoreCase(MessageType.BROADCAST)){
						//Broadcast to all, show in broadcast UI
						showBroadcastMsg(msg);
					}else if(msg.getType().equalsIgnoreCase(MessageType.CLIENT_CLOSE)){
						msgReceiveThreadconnected = false;
					}else if(msg.getType().equalsIgnoreCase(MessageType.INIT_DIALOG)){
						openNewChatUI(msg);
					}else if(msg.getType().equalsIgnoreCase(MessageType.INIT_COMPLETE)){
						//Put reveiver chatUI in chatUIMap
						reveiverHasMyChatUI.put(msg.getSender(), true);
					}else if(msg.getType().equalsIgnoreCase(MessageType.DIALOG_CLOSE)){
						//Close dialog on my side
						closeChatWhenSenderClose(msg);
					}else if(msg.getType().equalsIgnoreCase(MessageType.FRIEND_LIST_UPDATE)){
						//Update friend list
						updatedFriendList(msg);
					}else if(msg.getType().equalsIgnoreCase(MessageType.SERVER_SHUTDOWN)){
						//Notice user and close all open windows
						//Send confirmation back to server and disconnect server, close all resources
						msgReceiveThreadconnected = false;
						serverShutdownConfirmation = true;
						serverShutdown(msg);
					}else if(msg.getType().equalsIgnoreCase(MessageType.PARTICIPANT_EXIT)){
						//Close corresponding dialog window
						closeChatWhenSenderExitSystem(msg);
					}
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		public boolean isMsgReceiveThreadconnected() {
			return msgReceiveThreadconnected;
		}
		public void setMsgReceiveThreadconnected(boolean msgReceiveThreadconnected) {
			this.msgReceiveThreadconnected = msgReceiveThreadconnected;
		}
	}

	public String getUsername() {
		return username;
	}
	
	public Map<String, ChatUI> getChatUIMap() {
		return chatUIMap;
	}
	
	public Map<String, Boolean> getReveiverHasMyChatUI() {
		return reveiverHasMyChatUI;
	}

	public boolean isServerShutdownConfirmation() {
		return serverShutdownConfirmation;
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}
}
