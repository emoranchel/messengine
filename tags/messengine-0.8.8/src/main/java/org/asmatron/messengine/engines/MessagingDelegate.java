package org.asmatron.messengine.engines;

import java.util.concurrent.Future;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public interface MessagingDelegate extends BaseDelegate {

  void send(Message<?> message);

  Future<Message<?>> request(Message<?> message, String responseType, long timeout);

  <T> MessageListener<T> addMessageListener(String type, MessageListener<T> listener);

  void removeMessageListener(String type, MessageListener<?> listener);

}
