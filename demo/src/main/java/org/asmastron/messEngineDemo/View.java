package org.asmastron.messEngineDemo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.asmatron.messengine.ViewEngine;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.annotations.EventMethod;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;
	private ViewEngine viewEngine;

	public View(ViewEngine engine) {
		this.viewEngine = engine;
		intialize();
	}

	private void intialize() {
		this.setSize(150, 80);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button = new JButton("CLICK ME!");
		this.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = JOptionPane.showInputDialog("Give me a username");
				ValueAction<String> actionArgument = new ValueAction<String>(username);
				ActionId<ValueAction<String>> actionId = new ActionId<ValueAction<String>>("SEARCH_USER");
				viewEngine.send(actionId, actionArgument);
			}
		});
	}

	@EventMethod("USER_FOUND")
	public void userFound(String username) {
		JOptionPane.showConfirmDialog(this, "User " + username + " found");
	}

	@EventMethod("USER_NOT_FOUND")
	public void userNotFound() {
		JOptionPane.showConfirmDialog(this, "User not found. try again.");
	}
}
