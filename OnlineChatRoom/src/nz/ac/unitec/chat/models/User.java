package nz.ac.unitec.chat.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable{
	private String userName;
	private String password;
	
	public User(String name, String password){
		this.userName = name;
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
