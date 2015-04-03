package org.asmatron.messengine.engines.components;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public class MessageConsumer<T> implements Runnable {

  private final Message<T> message;
  private final List<MessageListener<T>> listeners;

  private final static Logger log = Logger.getLogger(MessageConsumer.class.getName());

  public MessageConsumer(Message<T> message, List<MessageListener<T>> listeners) {
    this.message = message;
    this.listeners = listeners;
  }

  @Override
  public void run() {
    listeners.stream().forEach((messageListener) -> {
      try {
        log.log(Level.INFO, "\nMESSAGE:{0}:{1}", new Object[]{message.getType(), messageListener.getClass().getSimpleName()});
        messageListener.onMessage(message);
      } catch (Exception e) {
        log.log(Level.SEVERE, "Unhandled exception for a message of type " + message.getType() + " in listener " + messageListener, e);
      }
    });
  }
}
