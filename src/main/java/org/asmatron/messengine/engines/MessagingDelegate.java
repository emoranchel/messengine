package org.asmatron.messengine.engines;

import java.util.concurrent.Future;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public interface MessagingDelegate extends BaseDelegate {

  void send(Message<?> message);

  Future<Message<?>> request(Message<?> message, String responseType, long timeout);

  void addMessageListener(String type, MessageListener<?> listener);

  void removeMessageListener(String type, MessageListener<?> listener);

}
