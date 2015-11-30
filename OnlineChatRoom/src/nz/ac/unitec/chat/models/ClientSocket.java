package nz.ac.unitec.chat.models;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {
	
	private Socket clientSocket = null;
	
	public Socket init(){
		try {
			if(clientSocket == null){
				clientSocket = new Socket("localhost", 9090);
			}
			System.out.println("client connected");
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientSocket;
	}
	
}
