package org.asmatron.messengine.engines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.asmatron.messengine.engines.DefaultMessagingDelegate;
import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.testing.TestMessage;
import org.junit.Test;

public class IntegrationTestMessEngine {
	@Test(timeout = 500)
	public void shouldListenFooMessage() throws Exception {
		final Semaphore lock = new Semaphore(0);
		final StringBuilder actualBody = new StringBuilder();
		final String expectedBody = "Hello World!";
		DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
		engine.start();
		MessageListener<Message<String>> listener = new MessageListener<Message<String>>() {

			@Override
			public void onMessage(Message<String> message) {
				actualBody.append((String) message.getBody());
				lock.release();
			}
		};
		final String type = "test";
		engine.addMessageListener(type, listener);
		engine.send(new TestMessage<String>(type, expectedBody));
		lock.tryAcquire(3, TimeUnit.SECONDS);
		assertEquals(expectedBody, actualBody.toString());
		engine.stop();
	}

	@Test(timeout = 1000)
	public void shouldListen1000FooMessages() throws Exception {
		final Semaphore lock = new Semaphore(0);
		final String expectedBody = "HELLO WORLD!";
		DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
		engine.start();
		final AtomicInteger counter = new AtomicInteger();
		final MessageListener<Message<String>> listener = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
				lock.release();
			}
		};
		final String type = "foo";
		Message<String> message = new TestMessage<String>(type, expectedBody);
		engine.addMessageListener(type, listener);
		final int totalMessages = 1000;
		for (int i = 0; i < totalMessages; i++) {
			engine.send(message);
		}
		lock.tryAcquire(totalMessages, 3, TimeUnit.SECONDS);
		assertEquals(totalMessages, counter.get());
		engine.stop();
	}

	@Test(timeout = 1000)
	public void shouldHaveManyListenersForTheSameMessageType() throws Exception {
		final Semaphore lock = new Semaphore(0);
		DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
		engine.start();
		final AtomicInteger counter = new AtomicInteger();
		final MessageListener<Message<String>> listenerA = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
				lock.release();
			}
		};
		final MessageListener<Message<String>> listenerB = new MessageListener<Message<String>>() {
			@Override
			public void onMessage(Message<String> message) {
				counter.incrementAndGet();
				lock.release();
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
		lock.tryAcquire(totalMessages * 2, 3, TimeUnit.SECONDS);
		assertEquals(totalMessages * 2, counter.get());
		engine.stop();
	}

	@Test
	public void shouldStopListeningToMessage() throws Exception {
		DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
		engine.start();
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
		engine.stop();
	}

	@Test
	public void shouldRequestAndBlockUntilResponse() throws Exception {
		final DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
		engine.start();

		final int timeout = 5000;
		final int responseDelay = 1000;
		String responseType = "responseType";
		final Message<?> expectedResponse = new TestMessage<String>(responseType, "responseBody");
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

		Future<Message<?>> future = engine.request(new TestMessage<String>("requestType", "requestBody"), responseType,
				timeout);
		asyncResponseThread.start();
		Message<?> actualResponse = future.get();

		long totalTime = System.currentTimeMillis() - startTime;
		assertTrue(totalTime < timeout);
		assertTrue(totalTime >= responseDelay);
		assertEquals(expectedResponse, actualResponse);
		engine.stop();
	}

	@Test
	public void shouldRequestAndBlockUntilTimeout() throws Exception {
		final DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
		engine.start();
		final int timeout = 300;
		long startTime = System.currentTimeMillis();

		Future<Message<?>> future = engine.request(new TestMessage<String>("requestType", "requestBody"), "responseType",
				timeout);
		Message<?> actualResponse = future.get();

		long totalTime = System.currentTimeMillis() - startTime;
		assertTrue(totalTime >= timeout);
		assertNull(actualResponse);
		engine.stop();
	}

}
