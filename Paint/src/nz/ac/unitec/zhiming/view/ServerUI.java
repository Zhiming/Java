package nz.ac.unitec.zhiming.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nz.ac.unitec.zhiming.model.Server;

public class ServerUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JButton startServer;
	private JButton stopServer;
	private Server server;

	// private Server server = null;

	@SuppressWarnings("unused")
	public ServerUI() {
		panel = new JPanel();
		startServer = new JButton("Start Server");
		stopServer = new JButton("Stop Server");

		panel.add(startServer);
		panel.add(stopServer);
		this.add(panel);
		final ServerUI _this = this;

		// Bind listeners
		startServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server = new Server();
				server.startServer();
//				server.bindServerUI(_this);
			}
		});

		stopServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server.shutDownServer();
			}
		});

		this.setSize(400, 100);
		this.setResizable(false);
		this.setTitle("Server");
		this.setLocation(683, 384);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new ServerUI();
	}
}
