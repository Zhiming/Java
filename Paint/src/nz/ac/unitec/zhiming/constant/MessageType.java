package nz.ac.unitec.zhiming.constant;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MessageType implements Serializable {
	public static final String SIGN_IN = "SIGN_IN";
	public static final String SIGN_IN_CONFIRMATION = "SIGN_IN_CONFIRMATION";
	public static final String CLIENT_CLOSE = "CLIENT_CLOSE";
	public static final String BROADCAST = "BROADCAST";
	public static final String FRIEND_LIST_UPDATE = "FRIEND_LIST_UPDATE";
	public static final String SERVER_SHUTDOWN = "SERVER_SHUTDOWN";
	public static final String SERVER_SHUTDOWN_RECEIVED_CONFIRMATION = "SERVER_SHUTDOWN_RECEIVED_CONFIRMATION";
	public static final String SHAPE = "SHAPE";
}
