package nz.ac.unitec.chat.threads;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nz.ac.unitec.chat.constants.MessageReceiver;
import nz.ac.unitec.chat.constants.MessageType;
import nz.ac.unitec.chat.models.Message;

public class ThreadToUpdateFriendList implements Runnable{
	
	private Map<String, ThreadForClient> clientPool = null;
	private boolean serverStatus = false;
	private String previousFriendList = null;
	
	public ThreadToUpdateFriendList(Map<String, ThreadForClient> clientPool){
		this.clientPool = clientPool;
	}

	public boolean isServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(boolean serverStatus) {
		this.serverStatus = serverStatus;
	}
	
	private void sendUpdatedFriendList(String friendList){
		//Send complete friend list to each user
		Iterator<Entry<String, ThreadForClient>> it = clientPool.entrySet().iterator();
		Message msg = new Message(MessageType.FRIEND_LIST_UPDATE, friendList, MessageReceiver.SERVER, null, null);
		while(it.hasNext()){
			ThreadForClient clientThread = it.next().getValue();
			String receiver = clientThread.getUser().getUserName();
			String time = new Date().toString();
			msg.setReceiver(receiver);
			msg.setTime(time);
			try {
				clientThread.getOos().writeObject(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		System.out.println("serverStatus " + serverStatus);
		while(serverStatus == true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//No need to run if the server is closing when the thread wakes up
			if(serverStatus == true){
				if(clientPool.size() != 0){
					StringBuilder sb = new StringBuilder();
					Iterator<Entry<String, ThreadForClient>> it = clientPool.entrySet().iterator();
					//Get all friend names
					while(it.hasNext()){
						sb.append(it.next().getValue().getUser().getUserName()+ "|");
					}
					String tempFriendList = sb.toString();
					String allFriendList = tempFriendList.substring(0, tempFriendList.lastIndexOf("|"));
					//Check whether the new friend list is the same with previous one 
					if(previousFriendList == null){
						//No one logged in before
						sendUpdatedFriendList(allFriendList);
						previousFriendList = allFriendList;
					}else{
						//New users logged in
						if(previousFriendList.hashCode() != allFriendList.hashCode()){
							sendUpdatedFriendList(allFriendList);
							previousFriendList = allFriendList;
						}
					}
				}else if(clientPool.size() == 0){
					previousFriendList = "";
				}
			}
		}
		
	}
}
