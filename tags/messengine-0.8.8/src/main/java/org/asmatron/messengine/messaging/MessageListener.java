package org.asmatron.messengine.messaging;

public interface MessageListener<T> {

  void onMessage(Message<T> message);
}
