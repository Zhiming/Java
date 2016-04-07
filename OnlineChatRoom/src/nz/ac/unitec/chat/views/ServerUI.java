package nz.ac.unitec.chat.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nz.ac.unitec.chat.models.Server;

@SuppressWarnings("serial")
public class ServerUI extends JFrame{
	private JPanel panel = null;
	private JButton startServer = null;
	private JButton stopServer = null;
	private Server server = null;
	
	public ServerUI(){
		panel = new JPanel();
		startServer = new JButton("Start Server");
		stopServer = new JButton("Stop Server");
		
		panel.add(startServer);
		panel.add(stopServer);
		this.add(panel);
		final ServerUI _this = this;
		
		//Bind listeners
		startServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server = new Server();
				server.startServer();
				server.bindServerUI(_this);
			}
		});
		
		stopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(server != null){
					server.shutDownServer();
					server = null;
				}else{
					JOptionPane.showMessageDialog(null, "The server is not started yet");
				}
			}
		});
		
		this.setSize(400, 100);
		this.setResizable(false);
		this.setTitle("Server for Zhiming and his friends");
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(server != null){
					server.shutDownServer();
					server = null;
				}
				_this.dispose();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				
			}
		});
		this.setVisible(true);
	}
	
public static void main(String[] args) {
	new ServerUI();
}
}
