package nz.ac.unitec.chat.views;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import nz.ac.unitec.chat.constants.GlobalConstants;
import nz.ac.unitec.chat.controllers.ClientTool;

@SuppressWarnings("serial")
public class ClientLoginUI extends JFrame{
	
	//Connection tool
	private ClientTool clientTool = null;
	
	//Account
	private JPanel pAccountNo = null;
	private JLabel lAccountNo = null;
	private JTextField tAccountNo = null;
	private ClientLoginUI _this = null;

	//Password
	private JPanel pPassword = null;
	private JLabel lPassword = null;	
	private JPasswordField passwordField = null;
	
	//Server IP
	private JPanel pServer = null;
	private JLabel lServerIP = null;
	private JTextField tServerIP = null;
//	private JPanel ipInputPanel = null;
//	private JLabel defaultIp = null;
	
	//Buttons
	private JPanel pButtons = null;
	private JButton bSignIn = null;
	private JButton bRegister = null;
	private JButton bCancel = null;
	
	public ClientLoginUI(){
		
		_this = this;
		
		//Set account part
		pAccountNo = new JPanel();
		lAccountNo = new JLabel("Account Number: ");
		tAccountNo = new JTextField(10);
		pAccountNo.add(lAccountNo);
		pAccountNo.add(tAccountNo);
		
		//Set password
		pPassword = new JPanel();
		lPassword = new JLabel("             Password: ");
		passwordField = new JPasswordField(10);
		
		//Set server
		this.pServer = new JPanel();
		
		this.lServerIP = new JLabel("               Server IP: ");
		this.tServerIP = new JTextField(10);
//		this.ipInputPanel = new JPanel();
//		this.ipInputPanel.add(lServerIP);
//		this.ipInputPanel.add(tServerIP);
//		this.defaultIp = new JLabel("                                   localhost will be the server IP if leave blank");
//		pServer.add(ipInputPanel);
//		pServer.add(defaultIp);
		this.pServer.add(lServerIP);
		this.pServer.add(tServerIP);
		
		//Add key listener to password so that when enter is pressed, it will invoke login process
		passwordField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		pPassword.add(lPassword);
		pPassword.add(passwordField);
		
		
		//Set buttons
		pButtons = new JPanel();
		bSignIn = new JButton("Sign In");
		bRegister = new JButton("Register");
		bCancel = new JButton("Cancel");
		pButtons.add(bSignIn);
		pButtons.add(bRegister);
		pButtons.add(bCancel);
		
		//Add panels
		this.add(pAccountNo);
		this.add(pPassword);
		this.add(pServer);
		this.add(pButtons);
		
		
		
		//Bind listners to buttons
		bSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		bSignIn.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		bRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		
		bCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_this.dispose();
			}
		});
		
		//Set JFrame
		this.setLayout(new GridLayout(4,1));
		this.setSize(350, 180);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		try {
			this.setTitle(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		this.setLocation(683, 384);
		this.setVisible(true);
	}
	
	private void register(){
		String name = tAccountNo.getText().trim();
		if(name.equals("") || name.length() == 0){
			JOptionPane.showMessageDialog(null, "Please enter password");
			return;
		}
		String password = new String(passwordField.getPassword());
		if(password.equals("") || password.length() == 0){
			JOptionPane.showMessageDialog(null, "Please enter password");
			return;
		}
		String serverIp = new String(tServerIP.getText().trim());
//		if(serverIp.equals("") || serverIp.length() == 0){
//			JOptionPane.showMessageDialog(null, "Please enter server IP address");
//			return;
//		}
		this.clientTool = new ClientTool(name, serverIp);
		if(this.clientTool.getClientSocket() != null){
			String registerResult = clientTool.userRegister(name, password);
			if(registerResult.equals(GlobalConstants.USER_EXISTED)){
				JOptionPane.showMessageDialog(null, "The username has existed, please change it");
			}else{
				JOptionPane.showMessageDialog(null, "Regstration completed successfully, please sign in");
			}
		}else{
			JOptionPane.showMessageDialog(null, "Wrong server IP or the server is not started yet");
		}
	}
	
	private void login(){
		String name = tAccountNo.getText().trim();
		if(name.equals("") || name.length() == 0){
			JOptionPane.showMessageDialog(null, "Please enter username");
			return;
		}
		String password = new String(passwordField.getPassword());
		if(password.equals("") || password.length() == 0){
			JOptionPane.showMessageDialog(null, "Please enter password");
			return;
		}
		String serverIp = new String(tServerIP.getText().trim());
//		if(serverIp.equals("") || serverIp.length() == 0){
//			JOptionPane.showMessageDialog(null, "Please enter server IP address");
//			return;
//		}
		this.clientTool = new ClientTool(name, serverIp);
		if(this.clientTool.getClientSocket() != null){
			String authorizationResult = clientTool.loginValidate(name,password);
			if(authorizationResult.equals(GlobalConstants.AUTHORIZATION_GRANTED)){
				_this.dispose();
				new FriendListUI(this.clientTool);
			}else if(authorizationResult.equals(GlobalConstants.AUTHORIZATION_DENIED)){
				JOptionPane.showMessageDialog(null, "Wrong User name or password");
			}else if(authorizationResult.equals(GlobalConstants.DUPLICATE_LOGIN)){
				JOptionPane.showMessageDialog(null, "Duplicate sign in, please sign in with another name");
			}else{
				JOptionPane.showMessageDialog(null, "User doesn't exit, please register");
			}
		}else{
			JOptionPane.showMessageDialog(null, "Wrong server IP or the server is not started yet");
		}
	}
	
public static void main(String[] args) {
	new ClientLoginUI();
}
	
}
