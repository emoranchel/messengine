package org.asmatron.messengine;

import java.util.concurrent.Future;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public interface MessEngine {

  void send(Message<?> message);

  Future<Message<?>> request(Message<?> message, String responseType, long timeout);

  void addMessageListener(String type, MessageListener<Message<?>> listener);

  void removeMessageListener(String type, MessageListener<Message<?>> listener);
}
