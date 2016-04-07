package nz.ac.unitec.zhiming.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nz.ac.unitec.zhiming.util.ClientTool;

public class SignInUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField jTextField;
	private JLabel lblUserName;
	private JPanel pnlTxtArea, pnlButton;
	private JButton btnSignIn, btnCancel;
	private SignInUI _this;
	private ClientTool clientTool;

	public SignInUI() {

		_this = this;
		jTextField = new JTextField(12);
		lblUserName = new JLabel("User Name: ");
		btnSignIn = new JButton("Sign In");
		btnCancel = new JButton("Cancel");
		pnlTxtArea = new JPanel();
		pnlButton = new JPanel();

		this.setLayout(new GridLayout(2, 1));

		pnlTxtArea.add(lblUserName);
		pnlTxtArea.add(jTextField);

		pnlButton.add(btnSignIn);
		pnlButton.add(btnCancel);

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_this.dispose();
			}
		});

		btnSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (usernameCheck()) {
					clientTool = new ClientTool(jTextField.getText().trim());
					if (clientTool.connect()) {
						_this.dispose();
						// show canvas
						new CanvasUI(clientTool);
					} else {
						JOptionPane.showMessageDialog(null,
								"Failed to connect to server, please contact administor");
					}
				}
			}
		});

		this.add(pnlTxtArea);
		this.add(pnlButton);
		this.setSize(300, 150);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sign In");
		this.setLocation(683, 384);
		this.setVisible(true);
	}

	private boolean usernameCheck() {
		if (jTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter username");
			return false;
		} else {
			return true;
		}
	}
	
	public static void main(String[] args) {
		new SignInUI();
	}
}
