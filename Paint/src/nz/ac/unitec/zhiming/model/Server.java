package nz.ac.unitec.zhiming.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JOptionPane;

import nz.ac.unitec.zhiming.constant.MessageReceiver;
import nz.ac.unitec.zhiming.constant.MessageType;
import nz.ac.unitec.zhiming.thread.ThreadForClient;
import nz.ac.unitec.zhiming.thread.ThreadToUpdateFriendList;
import nz.ac.unitec.zhiming.util.ServerTool;

public class Server {

	private ServerSocket serverSocket;
	private Map<User, ThreadForClient> clientPool;
	private boolean serverRunning = false;
	private ServerTool serverTool;
	private ConnectionRequestThread clientConnectionHandleThread;
	private ThreadToUpdateFriendList friendListUpdateThread;
	private HashSet<ShapeEntity> drawnHistory;

	public void startServer() {
		try {
			clientPool = new HashMap<User, ThreadForClient>();
			drawnHistory = new HashSet<ShapeEntity>();
			friendListUpdateThread = new ThreadToUpdateFriendList(clientPool);
			this.serverSocket = new ServerSocket(9090);
			this.clientConnectionHandleThread = new ConnectionRequestThread();
			this.serverTool = new ServerTool();
			this.friendListUpdateThread.setServerStatus(true);
			new Thread(friendListUpdateThread).start();
			new Thread(this.clientConnectionHandleThread).start();
			this.serverRunning = true;
			System.out.println("Server is running...");
		} catch (IOException e) {
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
			}
			
			//Close all resources and threads
			while(this.clientPool.size() != 0){
				Thread.sleep(100);
			}
			
			this.serverRunning = false;

			//Stop request processing thread
			if(serverSocket != null){
				stopRequestThread();
			}
			
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

	private void stopRequestThread(){
		try {
			Socket socket = new Socket("localhost", 9090);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(MessageType.SERVER_SHUTDOWN, MessageType.SERVER_SHUTDOWN,
					new User(MessageReceiver.SERVER), new User(MessageReceiver.SERVER), new Date().toString()));
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
		}
	}

	private class ConnectionRequestThread implements Runnable {
		@Override
		public void run() {
			while (serverRunning) {
				try {
					Socket clientSocket = serverSocket.accept();
					ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
					Message msg = (Message) ois.readObject();
					if (msg.getType().equals(MessageType.SIGN_IN)) {
						// Assign a thread for a client
						ThreadForClient clientThread = new ThreadForClient(
								clientSocket, ois, oos, msg.getSender(),
								clientPool, drawnHistory);
						clientThread.start();
//						System.out.println("client logged");
						clientPool.put(clientThread.getUser(), clientThread);
					} else if (msg.getType().equals(MessageType.SERVER_SHUTDOWN)) {
						//自己发送的请求，用于关闭服务器
						//this request comes from server itself to close the while loop
						serverRunning = false;
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new Server().startServer();
	}
}
