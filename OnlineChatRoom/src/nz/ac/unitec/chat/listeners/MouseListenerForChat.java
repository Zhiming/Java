package nz.ac.unitec.chat.listeners;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import nz.ac.unitec.chat.controllers.ClientTool;
import nz.ac.unitec.chat.views.ChatUI;

public class MouseListenerForChat implements MouseListener{
	
	private ClientTool clientTool = null;
	
	public MouseListenerForChat(ClientTool clientTool){
		this.clientTool = clientTool;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2){
			String receiver = ((JLabel)e.getSource()).getText();
			JLabel source = (JLabel)e.getSource();
			//Disable double click event to prevent from multiple chat windows
			source.removeMouseListener(this);
			new ChatUI(receiver, clientTool, source);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JLabel label = (JLabel)e.getSource();
		label.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JLabel label = (JLabel)e.getSource();
		label.setForeground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
