package nz.ac.unitec.chat.models;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class ConnectionPool {
	
	private static Map<String, Socket> connections = new HashMap<String, Socket>();
	
	public static Socket getConnection(String username, String serverIp){
		Socket clientSocket = null;
		if(serverIp == null || serverIp.equalsIgnoreCase("") || serverIp == "" || serverIp.length() == 0){
			serverIp = "localhost";
		}
		try{
			if(connections.get(username) == null){
				clientSocket = new Socket(serverIp, 9090);
				connections.put(username, clientSocket);
			}else{
				clientSocket = connections.get(username);
			}
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, "Wrong Server IP");
		}
		catch (ConnectException e){
			clientSocket = null;
//			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return clientSocket;
	}
	
	public static void removeConnection(String username){
		connections.remove(username);
	}
	
//public static void main(String[] args) {
//	ConnectionPool.getConnection("zhiming");
//}
}
