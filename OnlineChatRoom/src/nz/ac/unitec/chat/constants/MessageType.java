package nz.ac.unitec.chat.constants;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MessageType implements Serializable {
	public static final String LOGIN_AUTHORIZATION = "LOGIN_AUTHORIZATION";
	public static final String CLIENT_CLOSE = "CLIENT_CLOSE";
	public static final String REGULAR_PRIVATE = "REGULAR_PRIVATE";
	public static final String BROADCAST = "BROADCAST";
	public static final String INIT_DIALOG = "INIT_DIALOG";
	public static final String INIT_COMPLETE = "INIT_COMPLETE";
	public static final String DIALOG_CLOSE = "DIALOG_CLOSE";
	public static final String FRIEND_LIST_UPDATE = "FRIEND_LIST_UPDATE";
	public static final String SERVER_SHUTDOWN = "SERVER_SHUTDOWN";
	public static final String SERVER_SHUTDOWN_RECEIVED_CONFIRMATION = "SERVER_SHUTDOWN_RECEIVED_CONFIRMATION";
	public static final String PARTICIPANT_EXIT = "PARTICIPANT_EXIT";
	public static final String USER_REGISTRATION = "USER_REGISTRATION";
}
