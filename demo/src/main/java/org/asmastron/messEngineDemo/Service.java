package org.asmastron.messEngineDemo;

import org.asmatron.messengine.ControlEngine;
import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.annotations.ActionMethod;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.messaging.SimpleMessage;

public class Service {
	private final ControlEngine controlEngine;
	private final MessEngine messEngine;

	public Service(ControlEngine controlEngine, MessEngine messEngine) {
		this.controlEngine = controlEngine;
		this.messEngine = messEngine;
	}

	@ActionMethod(Api.Actions.SEARCH_USER_ID)
	public void searchUser(String username) {
		// complex logic
		if (username.startsWith("e") || username.startsWith("E")) {
			ValueEvent<String> argument = new ValueEvent<String>(username);
			controlEngine.fireEvent(Api.Events.USER_FOUND, argument);
		} else {
			controlEngine.fireEvent(Api.Events.USER_NOT_FOUND);
		}
		// we call other service because our method is overly complex
		messEngine.send(new SimpleMessage<Void>(Api.Messages.REMOTE_CALL, null));
	}
}
