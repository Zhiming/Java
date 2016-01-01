package nz.ac.unitec.zhiming.util;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nz.ac.unitec.zhiming.constant.MessageReceiver;
import nz.ac.unitec.zhiming.constant.MessageType;
import nz.ac.unitec.zhiming.model.Message;
import nz.ac.unitec.zhiming.model.User;
import nz.ac.unitec.zhiming.thread.ThreadForClient;

public class ServerTool {
	public void serverShutdownNotice(Map<User, ThreadForClient> clientPool){
		Iterator<Entry<User, ThreadForClient>> it = clientPool.entrySet().iterator();
		while(it.hasNext()){
			Entry<User, ThreadForClient> entry = it.next();
			User receiver = entry.getKey();
			ThreadForClient clientThread = entry.getValue();
			Message msg = new Message(MessageType.SERVER_SHUTDOWN, MessageType.SERVER_SHUTDOWN, 
					new User(MessageReceiver.SERVER), receiver, new Date().toString());
			clientThread.serverShutdownNotice(msg);
		}
	}
}
