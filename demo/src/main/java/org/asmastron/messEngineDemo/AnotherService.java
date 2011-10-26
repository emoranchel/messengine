package org.asmastron.messEngineDemo;

import javax.swing.JOptionPane;

import org.asmatron.messengine.annotations.MessageMethod;

public class AnotherService {
	@MessageMethod(Api.Messages.REMOTE_CALL)
	public void messageRemoteCall() {
		// Dont do this please, its a demo so its ok, but all swing must be handled
		// in swing.
		JOptionPane.showConfirmDialog(null, "this was called from the secondary service");
	}
}
