package org.asmatron.messengine.messengine.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.messaging.impl.DefaultMessEngine;
import org.asmatron.messengine.messaging.impl.MessageConsumer;
import org.asmatron.messengine.messaging.impl.ResponseManager;
import org.asmatron.messengine.messaging.impl.TestMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class TestDefaultMessEngine {
	@InjectMocks
	private DefaultMessEngine messEngine = new DefaultMessEngine();
	@Mock
	private MessageListener<Message<?>> listener;
	@Spy
	private Map<String, List<MessageListener<? extends Message<?>>>> listeners = new HashMap<String, List<MessageListener<? extends Message<?>>>>();
	@Mock
	private BlockingQueue<Message<String>> queue;
	@Mock
	private ExecutorService messageExecutor;
	@Mock
	private ExecutorService engineExecutor;
	@SuppressWarnings("unused")
	@Mock
	private ResponseManager responseManager;

	String testType = "test";
	@SuppressWarnings("unchecked")
	@Mock
	private Message message;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		setValueToPrivateField(messEngine, "messageExecutor", messageExecutor);
		setValueToPrivateField(messEngine, "engineExecutor", engineExecutor);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailBecauseMessageIsBodyLess() throws Exception {
		messEngine.send(message);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailBecauseMessageTypeIsInvalid() throws Exception {
		when(message.getBody()).thenReturn(new String());
		messEngine.send(message);
	}

	@Test
	public void shouldInitMessEngine() throws Exception {
		messEngine.init();
		verify(engineExecutor).execute(messEngine);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldSendAMessage() throws Exception {
		when(message.getBody()).thenReturn(new String());
		when(message.getType()).thenReturn("test");

		messEngine.send(message);

		verify(queue).offer(message);
	}

	@Test
	public void shouldAddAndRemoveListeners() throws Exception {
		assertNull(listeners.get(testType));
		messEngine.addMessageListener(testType, listener);
		assertFalse(listeners.get(testType).isEmpty());
		messEngine.removeMessageListener(testType, listener);
		assertTrue(listeners.get(testType).isEmpty());
	}

	@Test
	public void shouldProccessAMessage() throws Exception {
		when(engineExecutor.isShutdown()).thenReturn(false, true);
		messEngine.addMessageListener(testType, listener);
		TestMessage<String> message = new TestMessage<String>(testType, "hello world!");
		when(queue.take()).thenReturn(message);

		messEngine.run();

		verify(messageExecutor).execute(any(MessageConsumer.class));
	}

	@Test
	public void shouldIgnoreToMessageQueueInterruptions() throws Exception {
		when(engineExecutor.isShutdown()).thenReturn(false, true);
		when(queue.take()).thenThrow(new InterruptedException("May be a shutdown"));

		messEngine.run();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldReset() throws Exception {
		listeners.put(testType, new ArrayList<MessageListener<? extends Message<?>>>());
		List<Message<?>> unprocessedMessages = messEngine.reset();

		assertNotNull(unprocessedMessages);
		verify(queue).drainTo(any(Collection.class));
		assertTrue(listeners.isEmpty());
	}

	@Test
	public void shouldShutdown() throws Exception {
		messEngine.shutdown();

		verify(engineExecutor).shutdownNow();
		verify(messageExecutor).shutdownNow();
	}

	public void setValueToPrivateField(Object object, String fieldName, Object value) throws Exception {
		Field privateField = object.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);
		privateField.set(object, value);
	}

}
