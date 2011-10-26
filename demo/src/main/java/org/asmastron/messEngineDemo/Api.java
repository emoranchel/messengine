package org.asmastron.messEngineDemo;

import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.event.EmptyEvent;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.ValueEvent;

public interface Api {
	interface Events {
		String USER_FOUND_ID = "USER_FOUND";
		EventId<ValueEvent<String>> USER_FOUND = EventId.ev(USER_FOUND_ID);
		String USER_NOT_FOUND_ID = "USER_NOT_FOUND";
		EventId<EmptyEvent> USER_NOT_FOUND = EventId.ev(USER_NOT_FOUND_ID);

	}

	interface Actions {
		String SEARCH_USER_ID = "SEARCH_USER";
		ActionId<ValueAction<String>> SEARCH_USER = ActionId.cm(SEARCH_USER_ID);
	}

	interface Messages {
		String REMOTE_CALL = "REMOTE_CALL_MSGTYPE";
	}
}
