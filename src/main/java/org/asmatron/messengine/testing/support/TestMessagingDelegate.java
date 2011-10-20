package org.asmatron.messengine.testing.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.engines.MessagingDelegate;
import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public class TestMessagingDelegate implements MessagingDelegate {
	private List<Message<?>> sentMessages = new ArrayList<Message<?>>();
	private Map<Message<?>, Runnable> messageReactions = new HashMap<Message<?>, Runnable>();
	private Map<String, Runnable> typeReactions = new HashMap<String, Runnable>();
	private Map<String, List<MessageListener<Message<?>>>> listeners = new HashMap<String, List<MessageListener<Message<?>>>>();
	private Message<?> currentMessage = null;
	private Map<String, List<Message<?>>> responses = new HashMap<String, List<Message<?>>>();

	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addMessageListener(String type, MessageListener<? extends Message<?>> listener) {
		List<MessageListener<Message<?>>> list = listeners.get(type);
		if (list == null) {
			list = new CopyOnWriteArrayList<MessageListener<Message<?>>>();
			listeners.put(type, list);
		}
		list.add((MessageListener<Message<?>>) listener);
	}

	@Override
	public void removeMessageListener(String type, MessageListener<? extends Message<?>> listener) {
		if (listeners.containsKey(type)) {
			listeners.get(type).remove(listener);

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
			for (Message<?> responseMessage : typeResponses) {
				send(responseMessage);
			}
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
			for (MessageListener<Message<?>> listener : listeners.get(type)) {
				try {
					listener.onMessage(message);
				} catch (Exception e) {
					// IGNORE
				}
			}
		}
	}

	private void executeReaction(Runnable reaction) {
		Thread reactionThread = new Thread(reaction);
		reactionThread.start();
		try {
			reactionThread.join();
		} catch (InterruptedException e) {
			log.error(e, e);
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

	public List<MessageListener<Message<?>>> getMessageListeners(String type) {
		return listeners.get(type);
	}

	public void addMessageReaction(Message<?> message, Runnable reaction) {
		messageReactions.put(message, reaction);
	}

	public void addTypeReaction(String type, Runnable reaction) {
		typeReactions.put(type, reaction);
	}

	public List<Message<?>> getSentMessages() {
		return new ArrayList<Message<?>>(sentMessages);
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
		return new ArrayList<Message<?>>();
	}

	public void addTypeResponse(String type, Message<?> responseMessage) {
		List<Message<?>> typeResponses = responses.get(type);
		if (typeResponses == null) {
			typeResponses = new ArrayList<Message<?>>();
			responses.put(type, typeResponses);
		}
		typeResponses.add(responseMessage);
	}

}
