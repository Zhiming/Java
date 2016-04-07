package nz.ac.unitec.chat.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nz.ac.unitec.chat.constants.ApplicationPath;
import nz.ac.unitec.chat.constants.GlobalConstants;
import nz.ac.unitec.chat.constants.MessageReceiver;
import nz.ac.unitec.chat.constants.MessageType;
import nz.ac.unitec.chat.models.Message;
import nz.ac.unitec.chat.threads.ThreadForClient;

public class ServerTool {
	
//	public static void main(String[] args) {
//		new ServerTool().readAccountsIntoCache(ApplicationPath.ACCOUNT_INFO_PATH, null);
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("zhiming", "123");
//		map.put("jade", "123");
//		new ServerTool().writeAccountsIntoDisc(map);
//	}
	
	public void readAccountsIntoCache(String path, Map<String, String> accountsMap){
		BufferedReader br = null;
		try {
			File file = new File(path);
			br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String tempStr = null;
			while((tempStr = br.readLine()) != null){
				sb.append(tempStr + "|");
			}
			String accounts = sb.toString().substring(0, sb.toString().lastIndexOf("|"));
			String[] userPasswordArray = accounts.split("\\|");
//for(int i = 0; i < userPasswordArray.length; i++){
//	System.out.println(i+1 + " : " + userPasswordArray[i]);
//}
			if(accountsMap == null){
				accountsMap = new HashMap<String, String>();
			}
			for(String entity : userPasswordArray){
				String[] userPasswordPair = entity.split("=");
				accountsMap.put(userPasswordPair[0], userPasswordPair[1]);
			}
//Iterator<Entry<String, String>> it = accountsMap.entrySet().iterator();
//System.out.println(accountsMap.size());
//while(it.hasNext()){
//	Entry<String, String> accountEntry = it.next();
//	String userPasswordPair = accountEntry.getKey() + "=" + accountEntry.getValue();
//	System.out.println(userPasswordPair);
//}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeAccountsIntoDisc(Map<String, String> accountsMap){
		FileWriter fw = null;
		try {
			fw = new FileWriter(ApplicationPath.ACCOUNT_INFO_PATH);
			BufferedWriter bw = new BufferedWriter(fw);
			Iterator<Entry<String, String>> it = accountsMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> accountEntry = it.next();
				String userPasswordPair = accountEntry.getKey() + "=" + accountEntry.getValue();
//System.out.println(userPasswordPair);
				bw.write(userPasswordPair);
				bw.newLine();
				bw.flush();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String loginValidation(String username, String password, Map<String, String> cachedAccountsMap, Map<String, ThreadForClient> clientPool){
		if(cachedAccountsMap.get(username) == null){
			return GlobalConstants.USER_NOT_EXIST;
		}else{
			if(clientPool != null && clientPool.get(username) != null){
				//The user has logged in
				return GlobalConstants.DUPLICATE_LOGIN;
			}else{
				String pwd = cachedAccountsMap.get(username);
				if(password.equals(pwd)){
					return GlobalConstants.AUTHORIZATION_GRANTED;
				}else{
					return GlobalConstants.AUTHORIZATION_DENIED;
				}
			}
		}
	}
	
	public String userRegister(String username, String pwd, Map<String, String> cachedAccountsMap){
		if(cachedAccountsMap.get(username) != null){
			return GlobalConstants.USER_EXISTED;
		}else{
			cachedAccountsMap.put(username, pwd);
			return GlobalConstants.REGISTRATION_COMPLETE;
		}
	}
	
	public void serverShutdownNotice(Map<String, ThreadForClient> clientPool){
		Iterator<Entry<String, ThreadForClient>> it = clientPool.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, ThreadForClient> entry = it.next();
			String receiver = entry.getKey();
			ThreadForClient clientThread = entry.getValue();
			Message msg = new Message(MessageType.SERVER_SHUTDOWN, MessageType.SERVER_SHUTDOWN, MessageReceiver.SERVER, receiver, new Date().toString());
			clientThread.serverShutdownNotice(msg);
		}
	}
	
}
