package org.asmatron.messengine.engines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.testing.TestMessage;
import org.asmatron.messengine.testing.support.TestMessagingDelegate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class TestStubMessEngine {

	@InjectMocks
	private TestMessagingDelegate messEngine = new TestMessagingDelegate();
	private String textType = "text";
	private String body = "body";
	private String objectType = "object";

	private TestMessage<String> textMessage = new TestMessage<String>(textType, body);
	private boolean listenerExecuted = false;
	private MessageListener<TestMessage<String>> textListener = new MessageListener<TestMessage<String>>() {
		@Override
		public void onMessage(TestMessage<String> message) {
			listenerExecuted = true;
		}
	};

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	TestMessage<Serializable> objectMessage = new TestMessage<Serializable>(objectType, body);
	private MessageListener<TestMessage<Serializable>> objectListener = new MessageListener<TestMessage<Serializable>>() {
		@Override
		public void onMessage(TestMessage<Serializable> message) {
			listenerExecuted = true;
		}
	};

	@Test
	public void shouldRecordMessages() throws Exception {
		assertNull(messEngine.getCurrentMessage());
		assertNull(messEngine.getMessage(textType));

		messEngine.send(textMessage);
		messEngine.send(objectMessage);

		assertEquals(2, messEngine.sentMessagesCount());
		assertEquals(textMessage, messEngine.getMessage(0));
		assertEquals(textMessage, messEngine.getMessage(textType));
		assertEquals(objectMessage, messEngine.getMessage(1));
		assertEquals(objectMessage, messEngine.getMessage(objectType));
		assertEquals(objectMessage, messEngine.getCurrentMessage());
	}

	@Test
	public void shouldRecordListeners() throws Exception {
		messEngine.addMessageListener(textType, textListener);
		messEngine.addMessageListener(objectType, objectListener);

		assertTrue(messEngine.getMessageListeners(textType).contains(textListener));
		assertTrue(messEngine.getMessageListeners(objectType).contains(objectListener));
		Set<String> registeredTypes = messEngine.getRegisteredTypes();
		assertEquals(2, registeredTypes.size());
		assertTrue(registeredTypes.contains(textType));
		assertTrue(registeredTypes.contains(objectType));

		messEngine.removeMessageListener(objectType, objectListener);
		assertTrue(messEngine.getMessageListeners(objectType).isEmpty());
	}

	@Test
	public void shouldReset() throws Exception {
		messEngine.send(textMessage);
		messEngine.send(objectMessage);
		messEngine.addMessageListener(textType, textListener);
		messEngine.addMessageListener(objectType, objectListener);

		messEngine.reset();

		assertTrue(messEngine.getSentMessages().isEmpty());
		assertTrue(messEngine.getRegisteredTypes().isEmpty());
	}

	@Test
	public void shouldBeSynchronous() throws Exception {
		messEngine.addMessageListener(textType, textListener);

		messEngine.send(textMessage);

		assertTrue(listenerExecuted);
	}

	@Test
	public void shouldReactToAMessage() throws Exception {
		final AtomicBoolean reactionExecuted = new AtomicBoolean(false);
		messEngine.addMessageReaction(textMessage, new Runnable() {
			@Override
			public void run() {
				reactionExecuted.set(true);
			}
		});

		messEngine.send(textMessage);

		assertTrue(reactionExecuted.get());
	}

	@Test
	public void shouldReactToAMessageType() throws Exception {
		final AtomicBoolean reactionExecuted = new AtomicBoolean(false);
		messEngine.addTypeReaction(textType, new Runnable() {
			@Override
			public void run() {
				reactionExecuted.set(true);
			}
		});

		messEngine.send(textMessage);

		assertTrue(reactionExecuted.get());

	}

	@Test
	public void shouldRespondToAMessageType() throws Exception {
		messEngine.addTypeResponse(textType, objectMessage);

		messEngine.send(textMessage);

		assertEquals(2, messEngine.sentMessagesCount());
		assertEquals(objectMessage, messEngine.getCurrentMessage());
	}
}
