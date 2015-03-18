package org.asmatron.messengine.testing.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asmatron.messengine.engines.MessagingDelegate;
import org.asmatron.messengine.engines.components.MessageConsumer;
import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public class TestMessagingDelegate implements MessagingDelegate {

  private final List<Message<?>> sentMessages = new ArrayList<>();
  private final Map<Message<?>, Runnable> messageReactions = new HashMap<>();
  private final Map<String, Runnable> typeReactions = new HashMap<>();
  private final Map<String, List<MessageListener<?>>> listeners = new HashMap<>();
  private Message<?> currentMessage = null;
  private final Map<String, List<Message<?>>> responses = new HashMap<>();

  private final static Logger log = Logger.getLogger(TestMessagingDelegate.class.getName());

  @Override
  public void start() {
  }

  @Override
  public void stop() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> MessageListener<T> addMessageListener(String type, MessageListener<T> listener) {
    List<MessageListener<?>> list = listeners.get(type);
    if (list == null) {
      list = new CopyOnWriteArrayList<>();
      listeners.put(type, list);
    }
    list.add((MessageListener<Message<?>>) listener);
    return listener;
  }

  @Override
  public void removeMessageListener(String type, MessageListener<?> listener) {
    if (listeners.containsKey(type)) {
      List<MessageListener<?>> messageListeners = listeners.get(type);
      messageListeners.remove(listener);
    }
  }

  @Override
  public void send(Message<?> message) {
    sentMessages.add(message);
    currentMessage = message;
    notifyListeners(message);
    executeReactions(message);
    sendResponses(message);
  }

  @Override
  public Future<Message<?>> request(Message<?> message, String responseType, long timeout) {
    throw new UnsupportedOperationException("This engine does not support requests.");
  }

  private void sendResponses(Message<?> message) {
    List<Message<?>> typeResponses = responses.get(message.getType());
    if (typeResponses != null) {
      typeResponses.stream().forEach((responseMessage) -> {
        send(responseMessage);
      });
    }
  }

  private void executeReactions(Message<?> message) {
    Runnable reaction = messageReactions.get(message);
    if (reaction != null) {
      executeReaction(reaction);
    }
    reaction = typeReactions.get(message.getType());
    if (reaction != null) {
      executeReaction(reaction);
    }
  }

  private void notifyListeners(Message<?> message) {
    String type = message.getType();
    if (listeners.containsKey(type)) {
      new MessageConsumer(message, listeners.get(type)).run();
    }
  }

  private void executeReaction(Runnable reaction) {
    Thread reactionThread = new Thread(reaction);
    reactionThread.start();
    try {
      reactionThread.join();
    } catch (InterruptedException e) {
      log.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public int sentMessagesCount() {
    return sentMessages.size();
  }

  public Message<?> getMessage(int index) {
    return sentMessages.get(index);
  }

  public Message<?> getMessage(String type) {
    for (Message<?> message : sentMessages) {
      if (message.getType().equals(type)) {
        return message;
      }
    }
    return null;
  }

  public Set<String> getRegisteredTypes() {
    return listeners.keySet();
  }

  public List<MessageListener<?>> getMessageListeners(String type) {
    return listeners.get(type);
  }

  public void addMessageReaction(Message<?> message, Runnable reaction) {
    messageReactions.put(message, reaction);
  }

  public void addTypeReaction(String type, Runnable reaction) {
    typeReactions.put(type, reaction);
  }

  public List<Message<?>> getSentMessages() {
    return new ArrayList<>(sentMessages);
  }

  public Message<?> getCurrentMessage() {
    return currentMessage;
  }

  public List<Message<?>> reset() {
    sentMessages.clear();
    messageReactions.clear();
    typeReactions.clear();
    listeners.clear();
    currentMessage = null;
    return new ArrayList<>();
  }

  public void addTypeResponse(String type, Message<?> responseMessage) {
    List<Message<?>> typeResponses = responses.get(type);
    if (typeResponses == null) {
      typeResponses = new ArrayList<>();
      responses.put(type, typeResponses);
    }
    typeResponses.add(responseMessage);
  }

}
