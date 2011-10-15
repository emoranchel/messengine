package org.asmatron.messengine.messaging.support;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageMethod;

public class MessageMethodTester {
	public String val = null;

	@MessageMethod(TestTypes.messageId)
	public void handleEvent(Message<String> valueEvent) {
		val = valueEvent.getBody();
	}

}
