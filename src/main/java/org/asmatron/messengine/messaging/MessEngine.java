package org.asmatron.messengine.messaging;

import java.util.List;
import java.util.concurrent.Future;


public interface MessEngine {

	void send(Message<?> message);

	Future<Message<?>> request(Message<?> message, String responseType, long timeout);

	void addMessageListener(String type, MessageListener<? extends Message<?>> listener);

	void removeMessageListener(String type, MessageListener<? extends Message<?>> listener);

	List<Message<?>> reset();
}
