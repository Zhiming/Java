package nz.ac.unitec.chat.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import nz.ac.unitec.chat.constants.ApplicationPath;
import nz.ac.unitec.chat.constants.GlobalConstants;
import nz.ac.unitec.chat.constants.MessageReceiver;
import nz.ac.unitec.chat.constants.MessageType;
import nz.ac.unitec.chat.controllers.ServerTool;
import nz.ac.unitec.chat.threads.ThreadForClient;
import nz.ac.unitec.chat.threads.ThreadToUpdateFriendList;
import nz.ac.unitec.chat.views.ServerUI;

public class Server {
	
	private boolean serverRunning = false;
	private Map<String, ThreadForClient> clientPool = null;
	private ServerSocket serverSocket = null;
	private ThreadToUpdateFriendList friendListUpdateThread = null;
	private Map<String, String> cachedUserPasswordMap = null;
	private ServerTool serverTool = null;
	private ServerUI serverUI = null;
	private ConnectionRequestThread connectionRequestThread = null;
	private boolean connectionRequestThreadRunning = false;
	
	public void startServer(){
		clientPool = new HashMap<String, ThreadForClient>();
		friendListUpdateThread = new ThreadToUpdateFriendList(clientPool);
		try {
			this.serverSocket = new ServerSocket(9090);
			this.serverRunning = true;
			this.friendListUpdateThread.setServerStatus(true);
			this.cachedUserPasswordMap = new HashMap<String, String>();
			this.serverTool = new ServerTool();
			this.serverTool.readAccountsIntoCache(ApplicationPath.ACCOUNT_INFO_PATH, this.cachedUserPasswordMap);
			new Thread(friendListUpdateThread).start();
			this.connectionRequestThread =  new ConnectionRequestThread();
			new Thread(this.connectionRequestThread).start();
			this.connectionRequestThreadRunning = true;
			System.out.println("server is running...");
		} 
		catch (BindException e){
			JOptionPane.showMessageDialog(null, "The server already started");
		}
		catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to start server");
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to start server");
		}
	}
	
	public void bindServerUI(ServerUI serverUI){
		this.serverUI = serverUI;
	}
	
	private void disconnectClient(Socket clientSocket, ObjectInputStream ois, ObjectOutputStream oos){
		try{
			if(ois != null){
				ois.close();
			}
			if(oos != null){
				oos.close();
			}
			if(clientSocket != null){
				clientSocket.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void stopRequestThread(){
		try {
			Socket socket = new Socket("localhost", 9090);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(MessageType.SERVER_SHUTDOWN, MessageType.SERVER_SHUTDOWN, MessageReceiver.SERVER, MessageReceiver.SERVER, new Date().toString()));
			while(this.connectionRequestThreadRunning){
				Thread.sleep(100);
			}
			oos.close();
			socket.close();
		} 
		catch (ConnectException e){
			e.printStackTrace();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void shutDownServer(){
		try {
			if(this.serverTool != null){
				//Notice all logged in users
				if(clientPool != null){
					this.serverTool.serverShutdownNotice(clientPool);
				}
				
				//Write online logged in user account information to disk
				if(cachedUserPasswordMap != null){
					this.serverTool.writeAccountsIntoDisc(cachedUserPasswordMap);
				}
			}
			
			//Close all resources and threads
			while(this.clientPool.size() != 0){
				Thread.sleep(100);
			}
			//Stop request processing thread
			if(serverSocket != null){
				stopRequestThread();
			}
			
			this.serverRunning = false;
			if(serverSocket != null){
				this.serverSocket.close();
			}
			
			if(this.friendListUpdateThread != null){
				this.friendListUpdateThread.setServerStatus(false);
			}
			JOptionPane.showMessageDialog(null, "The server stopped");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isServerRunning() {
		return serverRunning;
	}

	public void setServerRunning(boolean cond) {
		serverRunning = cond;
	}
	
//public static void main(String[] args) {
//	new Server().startServer();
//}
	
	private class ConnectionRequestThread implements Runnable{
		@Override
		public void run() {
			while(serverRunning){
				try {
					Socket clientSocket = serverSocket.accept();
					
					ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
					Message msg = (Message)ois.readObject();
					if(msg.getType().equals(MessageType.LOGIN_AUTHORIZATION)){
						String validationResult = serverTool.loginValidation(msg.getSender(), msg.getContent(), cachedUserPasswordMap, clientPool);
						User user = new User(msg.getSender(), msg.getContent()); 
						if(validationResult.equals(GlobalConstants.AUTHORIZATION_GRANTED)){
							oos.writeObject(new Message(MessageType.LOGIN_AUTHORIZATION, GlobalConstants.AUTHORIZATION_GRANTED, MessageReceiver.SERVER, user.getUserName(), new Date().toString()));
							//Assign a thread for a client
							ThreadForClient clientThread = new ThreadForClient(clientSocket, ois, oos, user , clientPool);
							clientThread.start();
							clientPool.put(clientThread.getUser().getUserName(), clientThread);
						}else if(validationResult.equals(GlobalConstants.USER_NOT_EXIST)){
							//User name doesn't exist, need register
							oos.writeObject(new Message(MessageType.LOGIN_AUTHORIZATION, GlobalConstants.USER_NOT_EXIST, MessageReceiver.SERVER, user.getUserName(), new Date().toString()));
							disconnectClient(clientSocket, ois, oos);
						}else if(validationResult.equals(GlobalConstants.DUPLICATE_LOGIN)){
							//The user has logged in
							oos.writeObject(new Message(MessageType.LOGIN_AUTHORIZATION, GlobalConstants.DUPLICATE_LOGIN, MessageReceiver.SERVER, user.getUserName(), new Date().toString()));
							disconnectClient(clientSocket, ois, oos);
						}else{
							//Wrong account information
							oos.writeObject(new Message(MessageType.LOGIN_AUTHORIZATION, GlobalConstants.AUTHORIZATION_DENIED, MessageReceiver.SERVER, user.getUserName(), new Date().toString()));
							disconnectClient(clientSocket, ois, oos);
						}
					}else if(msg.getType().equals(MessageType.USER_REGISTRATION)){
						String registrationResult = serverTool.userRegister(msg.getSender(), msg.getContent(), cachedUserPasswordMap);
						if(registrationResult.equals(GlobalConstants.USER_EXISTED)){
							oos.writeObject(new Message(MessageType.USER_REGISTRATION, GlobalConstants.USER_EXISTED, MessageReceiver.SERVER, msg.getSender(), new Date().toString()));
						}else if(registrationResult.equals(GlobalConstants.REGISTRATION_COMPLETE)){
							oos.writeObject(new Message(MessageType.USER_REGISTRATION, GlobalConstants.REGISTRATION_COMPLETE, MessageReceiver.SERVER, msg.getSender(), new Date().toString()));
						}
						disconnectClient(clientSocket, ois, oos);
					}else if (msg.getType().equals(MessageType.SERVER_SHUTDOWN)){
						serverRunning = false;
						connectionRequestThreadRunning = false;
					}
					
				}catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
