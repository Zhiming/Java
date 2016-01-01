package nz.ac.unitec.zhiming.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nz.ac.unitec.zhiming.constant.MessageReceiver;
import nz.ac.unitec.zhiming.constant.MessageType;
import nz.ac.unitec.zhiming.model.Message;
import nz.ac.unitec.zhiming.model.ShapeEntity;
import nz.ac.unitec.zhiming.model.User;

public class ThreadForClient extends Thread {
	private Socket clientSocket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private User user = null;
	private Map<User, ThreadForClient> clientPool = null;
	private boolean connected = false;
	private User server;
	private HashSet<ShapeEntity> drawnHistory;

	public ThreadForClient(Socket clientSocket, ObjectInputStream ois,
			ObjectOutputStream oos, User user,
			Map<User, ThreadForClient> clientPool, HashSet<ShapeEntity> drawnHistory) {
		this.clientSocket = clientSocket;
		this.ois = ois;
		this.oos = oos;
		this.user = user;
		server = new User(MessageReceiver.SERVER);
		this.clientPool = clientPool;
		this.connected = true;
		this.drawnHistory = drawnHistory;
	}

	public void serverShutdownNotice(Message msg) {
		forwardMsg(msg.getReceiver(), msg);
	}

	private void forwardMsg(User receiver, Message msg) {
		try {
			ThreadForClient msgReceiverThread = clientPool.get(receiver);
			if (msgReceiverThread != null) {
				msgReceiverThread.getOos().writeObject(msg);
				msgReceiverThread.getOos().flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			removeClient();
		}
	}

	private void broadcast(Message msg) {
		if (clientPool.size() > 0) {
			Iterator<Entry<User, ThreadForClient>> clientThreadIter = clientPool
					.entrySet().iterator();
			while (clientThreadIter.hasNext()) {
				User receiver = clientThreadIter.next().getValue().getUser();
				forwardMsg(receiver, msg);
			}
		}
	}

	private void removeClient() {
		try {
			if (this.ois != null) {
				this.ois.close();
			}
			if (this.oos != null) {
				this.oos.close();
			}
			if (this.clientSocket != null) {
				this.clientSocket.close();
			}
			clientPool.remove(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveMsg() {
		while (connected) {
			try {
				Message msg = (Message) ois.readObject();
				if (msg != null && msg.getType().equalsIgnoreCase(MessageType.CLIENT_CLOSE)
						&& msg.getContent().equalsIgnoreCase(MessageType.CLIENT_CLOSE)) {
					oos.writeObject(new Message(MessageType.CLIENT_CLOSE, MessageType.CLIENT_CLOSE, 
							new User(MessageReceiver.SERVER), user, new Date().toString()));
					connected = false;
					// broadcast all signed user
					removeClient();
					msg = new Message(MessageType.BROADCAST, user.getUsername() + " left discussion",
							server, new User(MessageReceiver.ALL), new Date().toString());
					broadcast(msg);
				} else if (msg != null&& msg.getType().equalsIgnoreCase(MessageType.BROADCAST)) {
					// Broadcast
					broadcast(msg);
				} else if (msg != null
						&& msg.getType().equals(MessageType.SERVER_SHUTDOWN_RECEIVED_CONFIRMATION)) {
					connected = false;
					removeClient();
				}else if(msg != null && msg.getType().equalsIgnoreCase(MessageType.SHAPE)){
					Set<ShapeEntity> tempShapeSet = msg.getDrawnShapes();
					for(ShapeEntity se : tempShapeSet){
						this.drawnHistory.add(se);
					}
					HashSet<ShapeEntity> hs = new HashSet<ShapeEntity>();
					hs.addAll(this.drawnHistory);
					msg.setDrawnShapes(hs);
					broadcast(msg);
				}else {
					User receiver = msg.getReceiver();
					forwardMsg(receiver, msg);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				removeClient();
			} catch (IOException e) {
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

	public Map<User, ThreadForClient> getClientPool() {
		return clientPool;
	}

	public void setClientPool(Map<User, ThreadForClient> clientPool) {
		this.clientPool = clientPool;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

}
