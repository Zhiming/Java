package nz.ac.unitec.zhiming.model;

import java.io.Serializable;
import java.util.HashSet;

public class Message implements Serializable {

	private static final long serialVersionUID = -3686132711222347592L;

	
	private String type;
	private String content;
	private User sender;
	private User receiver;
	private String time;
	private HashSet<ShapeEntity> drawnShapes;

	public Message(String type, String content, User sender, User receiver,
			String time) {
		this.type = type;
		this.content = content;
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
	}

	public Message(String type, String content,
			HashSet<ShapeEntity> drawnShapes, User sender, User receiver,
			String time) {
		this.type = type;
		this.content = content;
		this.drawnShapes = drawnShapes;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public HashSet<ShapeEntity> getDrawnShapes() {
		return drawnShapes;
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", content=" + content + ", sender="
				+ sender + ", receiver=" + receiver + ", time=" + time
				+ ", drawnShapes=" + drawnShapes + "]";
	}

	public void setDrawnShapes(HashSet<ShapeEntity> drawnShapes) {
		this.drawnShapes = drawnShapes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((drawnShapes == null) ? 0 : drawnShapes.hashCode());
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (drawnShapes == null) {
			if (other.drawnShapes != null)
				return false;
		} else if (!drawnShapes.equals(other.drawnShapes))
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
