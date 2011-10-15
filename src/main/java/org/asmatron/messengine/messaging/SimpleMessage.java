package org.asmatron.messengine.messaging;

import org.asmatron.messengine.messaging.Message;

public class SimpleMessage<T> implements Message<T> {
	private final String type;
	private final T body;

	public SimpleMessage(String type, T body) {
		this.type = type;
		this.body = body;
	}

	@Override
	public T getBody() {
		return body;
	}

	@Override
	public String getProperty(String arg0) {
		return null;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void putProperty(String arg0, String arg1) {
	}

}
