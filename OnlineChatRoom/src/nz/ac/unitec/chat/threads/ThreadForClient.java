package nz.ac.unitec.chat.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nz.ac.unitec.chat.constants.GlobalConstants;
import nz.ac.unitec.chat.constants.MessageReceiver;
import nz.ac.unitec.chat.constants.MessageType;
import nz.ac.unitec.chat.models.Message;
import nz.ac.unitec.chat.models.User;

public class ThreadForClient extends Thread{
	private Socket clientSocket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private User user = null;
	private Map<String, ThreadForClient> clientPool = null;
	private boolean connected = false;
	//Record dialog launched by a user
	private Set<String> dialogLaunched = null;
	//Record dialog accepted by a user
	private Set<String> dialogAccepted = null;
	
	public ThreadForClient(Socket clientSocket, ObjectInputStream ois, ObjectOutputStream oos, User user, Map<String, ThreadForClient> clientPool){
		this.clientSocket = clientSocket;
		this.ois = ois;
		this.oos = oos;
		this.user = user;
		this.clientPool = clientPool;
		this.connected = true;
		//Dialogs accepted by the user
		this.dialogAccepted = new HashSet<String>();
		//Dialogs launched by the user
		this.dialogLaunched = new HashSet<String>();
	}
	
	public void serverShutdownNotice(Message msg){
		forwardMsg(msg.getReceiver(), msg);
	}
	
	private void forwardMsg(String receiver, Message msg){
		try {
			ThreadForClient msgReceiverThread = clientPool.get(receiver);
//System.out.println("msg: " + msg.getType() + " " + msg.getContent() + " " + msg.getSender() + " " + msg.getReceiver());
			if(msgReceiverThread != null){
				msgReceiverThread.getOos().writeObject(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
			removeClient();
		}
	}
	
	private void broadcast(Message msg){
		Iterator<Entry<String, ThreadForClient>> clientThreadIter = clientPool.entrySet().iterator();
		while(clientThreadIter.hasNext()){
			String receiver = clientThreadIter.next().getValue().getUser().getUserName();
			forwardMsg(receiver, msg);
		}
	}
	
	private void markLauncherSet(Message msg){
		this.dialogLaunched.add(msg.getReceiver());
		forwardMsg(msg.getReceiver(), msg);
	}
	
	private void markAccepterSet(Message msg){
		this.dialogAccepted.add(msg.getReceiver());
		forwardMsg(msg.getReceiver(), msg);
	}
	
	private void removeDialogMarkInSet(Set<String> targetSet, String nameRemoved){
		Iterator<String> it = targetSet.iterator();
		while(it.hasNext()){
			if(it.next().equals(nameRemoved)){
				it.remove();
			}
		}
	}
	
	private void removeMarkInActiveDialogSet(Message msg){
		String receiver = msg.getReceiver();
		//Search in active dialog sets to remove mark
		//Remove the mark on this side
		boolean removedFlag = false;
		String whichSetOnOtherSide = null;
		Iterator<String> it = this.dialogAccepted.iterator();
		while(it.hasNext()){
			if(it.next().equals(receiver)){
				it.remove();
				removedFlag = true;
				whichSetOnOtherSide = GlobalConstants.DIALOG_LAUNCHER;
			}
		}
		
		if(!removedFlag){
			it = this.dialogLaunched.iterator();
			while(it.hasNext()){
				if(it.next().equals(receiver)){
					it.remove();
					whichSetOnOtherSide = GlobalConstants.DIALOG_ACCEPTER;
				}
			}
		}
		
		//Remove the mark on the other side
		ThreadForClient clientThread = clientPool.get(receiver);
		if(whichSetOnOtherSide.equals(GlobalConstants.DIALOG_ACCEPTER)){
			Set<String> acceptedDialogSetOnOtherSide = clientThread.getDialogAccepted();
			it = acceptedDialogSetOnOtherSide.iterator();
			while(it.hasNext()){
				if(it.next().equals(msg.getSender())){
					it.remove();
				}
			}
		}else{
			Set<String> launchedDialogSetOnOtherSide = clientThread.getDialogLaunched();
			it = launchedDialogSetOnOtherSide.iterator();
			while(it.hasNext()){
				if(it.next().equals(msg.getSender())){
					it.remove();
				}
			}
		}
	}
	
	private List<String> removeMarksInDialogSets(Message msg){
		
		List<String> needToNoticeList = new LinkedList<String>();
		
		//Fetch active dialog and send notices to them
		for(String name : this.dialogAccepted){
			needToNoticeList.add(name);
			//The variable might be complex. This user, the one pointed by this keyword is a dialog accepter
			//Here I am trying to get the same dialog launcher, so the variable is launcherThread
			ThreadForClient launcherThread = clientPool.get(name);
			Set<String> dialogLaunchedSetByLauncher = launcherThread.getDialogLaunched();
			removeDialogMarkInSet(dialogLaunchedSetByLauncher, msg.getSender());
		}
		
		for(String name : this.dialogLaunched){
			needToNoticeList.add(name);
			//The variable might be complex. This user, the one pointed by this keyword is a dialog launcher
			//Here I am trying to get the same dialog accepter, so the variable is accepterThread
			ThreadForClient accepterThread = clientPool.get(name);
			Set<String> dialogAcceptedSetByAccepter = accepterThread.getDialogAccepted();
			removeDialogMarkInSet(dialogAcceptedSetByAccepter, msg.getSender());
		}
		
		return needToNoticeList;
	}
	
	private void forwardParticipantExitNotice(Message msg){
		List<String> needToNoticeList = removeMarksInDialogSets(msg);
		
		msg.setType(MessageType.PARTICIPANT_EXIT);
		msg.setContent(MessageType.PARTICIPANT_EXIT);
		
		//No need to send if no active dialog
		if(needToNoticeList.size() > 0){
			for(String name : needToNoticeList){
				msg.setReceiver(name);
				forwardMsg(name, msg);
			}
		}
	}
	
	private void removeClient(){
		try {
			if(this.ois != null){
				this.ois.close();
			}
			if(this.oos != null){
				this.oos.close();
			}
			if(this.clientSocket != null){
				this.clientSocket.close();
			}
			clientPool.remove(user.getUserName());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void receiveMsg(){
		while(connected){
			try {
				Message msg = (Message)ois.readObject();
				if(msg != null && msg.getType().equalsIgnoreCase(MessageType.CLIENT_CLOSE) && msg.getContent().equalsIgnoreCase(MessageType.CLIENT_CLOSE)){
//System.out.println(msg.getType() + "---" + msg.getContent());
					oos.writeObject(new Message(MessageType.CLIENT_CLOSE, MessageType.CLIENT_CLOSE, MessageReceiver.SERVER, msg.getSender(), new Date().toString()));
					connected = false;
					forwardParticipantExitNotice(msg);
					removeClient();
				}else if(msg != null && msg.getType().equalsIgnoreCase(MessageType.BROADCAST)){
					//Broadcast
					broadcast(msg);
				}else if(msg != null && msg.getType().equalsIgnoreCase(MessageType.INIT_DIALOG)){
					//Mark activeDialogsIndexedByChatLauncher and forward message
					markLauncherSet(msg);
				}else if(msg != null && msg.getType().equalsIgnoreCase(MessageType.INIT_COMPLETE)){
					//Mark activeDialogsIndexedByChatAccepter and forward message
					markAccepterSet(msg);
				}else if(msg != null && msg.getType().equalsIgnoreCase(MessageType.DIALOG_CLOSE)){
					//Remove a mark in dialogAcceptedByUser and dialogLaunchedByUser
					removeMarkInActiveDialogSet(msg);
					forwardMsg(msg.getReceiver(), msg);
				}else if(msg != null && msg.getType().equals(MessageType.SERVER_SHUTDOWN_RECEIVED_CONFIRMATION)){
					connected = false;
					removeClient();
				}else{
					String receiver = msg.getReceiver();
					forwardMsg(receiver, msg);
				}
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
				removeClient();
			} 
			catch (IOException e) {
				e.printStackTrace();
				removeClient();
			}
		}
	}
	
	@Override
	public void run() {
		receiveMsg();
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, ThreadForClient> getClientPool() {
		return clientPool;
	}

	public void setClientPool(Map<String, ThreadForClient> clientPool) {
		this.clientPool = clientPool;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public Set<String> getDialogLaunched() {
		return dialogLaunched;
	}

	public void setDialogLaunched(Set<String> dialogLaunched) {
		this.dialogLaunched = dialogLaunched;
	}

	public Set<String> getDialogAccepted() {
		return dialogAccepted;
	}

	public void setDialogAccepted(Set<String> dialogAccepted) {
		this.dialogAccepted = dialogAccepted;
	}
}
