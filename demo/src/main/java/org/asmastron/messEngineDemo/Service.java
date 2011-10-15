package org.asmastron.messEngineDemo;

import org.asmatron.messengine.ControlEngine;
import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.annotations.ActionMethod;
import org.asmatron.messengine.event.EmptyEvent;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.messaging.SimpleMessage;

public class Service {
	private final ControlEngine controlEngine;
	private final MessEngine messEngine;

	public Service(ControlEngine controlEngine, MessEngine messEngine) {
		this.controlEngine = controlEngine;
		this.messEngine = messEngine;
	}

	@ActionMethod("SEARCH_USER")
	public void searchUser(String username) {
		// complex logic
		if (username.startsWith("e") || username.startsWith("E")) {
			EventId<ValueEvent<String>> eventId = new EventId<ValueEvent<String>>("USER_FOUND");
			ValueEvent<String> argument = new ValueEvent<String>(username);
			controlEngine.fireEvent(eventId, argument);
		} else {
			EventId<EmptyEvent> eventId = new EventId<EmptyEvent>("USER_NOT_FOUND");
			controlEngine.fireEvent(eventId);
		}
		// we call other service because our method is overly complex
		messEngine.send(new SimpleMessage<Void>("REMOTE_CALL", null));
	}
}
