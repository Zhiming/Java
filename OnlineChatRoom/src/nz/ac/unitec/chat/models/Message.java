package nz.ac.unitec.chat.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{

	private String type;
	private String content;
	private String sender;
	private String receiver;
	private String time;
	
	public Message(String type, String content, String sender, String receiver, String time){
		this.type = type;
		this.content = content;
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
