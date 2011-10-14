package org.asmatron.messengine.messengine.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.messaging.impl.DefaultMessEngine;
import org.asmatron.messengine.messaging.impl.TestMessage;
import org.junit.Test;

public class IntegrationTestMessEngine {
	@Test(timeout = 500)
	public void shouldListenFooMessage() throws Exception {
		final StringBuilder actualBody = new StringBuilder();
		final String expectedBody = "Hello World!";
		DefaultMessEngine engine = new DefaultMessEngine();
		engine.init();
		MessageListener<Message<String>> listener = new MessageListener<Message<String>>() {

			@Override
			public void onMessage(Message<String> message) {
				actualBody.append((String) message.getBody());

			}
		};
		final String type = "test";
		engine.addMessageListener(type, listener);
		engine.send(new TestMessage<String>(type, expectedBody));
		Pause.pause(new Condition("Waiting for the message to be processed.") {
			@Override
			public boolean test() {
				return actualBody.length() > 0;
			}
		});
		assertEquals(expectedBody, actualBody.toString());
		engine.shutdown();
	}

	@Test(timeout = 1000)
	public void shouldListen1000FooMessages() throws Exception {
		final String expectedBody = "HELLO WORLD!";
		DefaultMessEngine engine = new DefaultMessEngine();
		engine.init();
		final AtomicInteger counter = new AtomicInteger();
		final MessageListener<Message<String>> listener = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
			}
		};
		final String type = "foo";
		Message<String> message = new TestMessage<String>(type, expectedBody);
		engine.addMessageListener(type, listener);
		final int totalMessages = 1000;
		for (int i = 0; i < totalMessages; i++) {
			engine.send(message);
		}
		Pause.pause(new Condition("Waiting for " + totalMessages
				+ " messages to complete.") {
			@Override
			public boolean test() {
				return counter.get() == totalMessages;
			}
		});
		engine.shutdown();
	}

	@Test(timeout = 1000)
	public void shouldHaveManyListenersForTheSameMessageType() throws Exception {
		DefaultMessEngine engine = new DefaultMessEngine();
		engine.init();
		final AtomicInteger counter = new AtomicInteger();
		final MessageListener<Message<String>> listenerA = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
			}
		};
		final MessageListener<Message<String>> listenerB = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
			}
		};
		final String type = "foo";
		final String expectedBody = "HELLO WORLD!";
		Message<String> message = new TestMessage<String>(type, expectedBody);
		engine.addMessageListener(type, listenerA);
		engine.addMessageListener(type, listenerB);
		final int totalMessages = 1000;
		for (int i = 0; i < totalMessages; i++) {
			engine.send(message);
		}
		
		Pause.pause(new Condition("Waiting for " + totalMessages
				+ " messages to complete.") {
			@Override
			public boolean test() {
				// should increment per message in both listeners
				return counter.get() == totalMessages * 2;
			}
		});
		engine.shutdown();
	}

	@Test
	public void shouldStopListeningToMessage() throws Exception {
		DefaultMessEngine engine = new DefaultMessEngine();
		engine.init();
		final AtomicInteger counter = new AtomicInteger();
		final MessageListener<Message<String>> listener = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
			}
		};
		String type = "foo";
		engine.addMessageListener(type, listener);
		engine.send(new TestMessage<String>(type, "body"));
		Thread.sleep(100);
		engine.removeMessageListener(type, listener);
		engine.send(new TestMessage<String>(type, "body"));
		Thread.sleep(100);
		assertEquals(1, counter.get());
		engine.shutdown();
	}

	@Test
	public void shouldRequestAndBlockUntilResponse() throws Exception {
		final DefaultMessEngine engine = new DefaultMessEngine();
		engine.init();

		final int timeout = 5000;
		final int responseDelay = 1000;
		String responseType = "responseType";
		final Message<?> expectedResponse = new TestMessage<String>(
				responseType, "responseBody");
		Thread asyncResponseThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(responseDelay);
				} catch (InterruptedException e) {
				}
				engine.send(expectedResponse);
			}
		});
		long startTime = System.currentTimeMillis();

		Future<Message<?>> future = engine.request(new TestMessage<String>(
				"requestType", "requestBody"), responseType, timeout);
		asyncResponseThread.start();
		Message<?> actualResponse = future.get();

		long totalTime = System.currentTimeMillis() - startTime;
		assertTrue(totalTime < timeout);
		assertTrue(totalTime >= responseDelay);
		assertEquals(expectedResponse, actualResponse);
		engine.shutdown();
	}

	@Test
	public void shouldRequestAndBlockUntilTimeout() throws Exception {
		final DefaultMessEngine engine = new DefaultMessEngine();
		engine.init();
		final int timeout = 300;
		long startTime = System.currentTimeMillis();

		Future<Message<?>> future = engine.request(new TestMessage<String>(
				"requestType", "requestBody"), "responseType", timeout);
		Message<?> actualResponse = future.get();

		long totalTime = System.currentTimeMillis() - startTime;
		assertTrue(totalTime >= timeout);
		assertNull(actualResponse);
		engine.shutdown();
	}

}
