package org.asmatron.messengine.engines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    final String type = "test";

    DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
    engine.start();

    engine.addMessageListener(type, (Message<String> message) -> {
      actualBody.append((String) message.getBody());
      lock.release();
    });

    engine.send(new TestMessage<>(type, expectedBody));

    lock.tryAcquire(3, TimeUnit.SECONDS);
    assertEquals(expectedBody, actualBody.toString());

    engine.stop();
  }

  @Test(timeout = 1000)
  public void shouldListen1000FooMessages() throws Exception {
    final Semaphore lock = new Semaphore(0);
    final String expectedBody = "HELLO WORLD!";
    final AtomicInteger counter = new AtomicInteger();
    final String type = "foo";
    final int totalMessages = 1000;

    DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
    engine.start();

    engine.addMessageListener(type, (Message<String> message) -> {
      counter.incrementAndGet();
      lock.release();
    });

    for (int i = 0; i < totalMessages; i++) {
      engine.send(new TestMessage<>(type, expectedBody));
    }

    lock.tryAcquire(totalMessages, 3, TimeUnit.SECONDS);
    assertEquals(totalMessages, counter.get());
    engine.stop();
  }

  @Test(timeout = 1000)
  public void shouldHaveManyListenersForTheSameMessageType() throws Exception {
    final Semaphore lock = new Semaphore(0);
    final AtomicInteger counterA = new AtomicInteger();
    final AtomicInteger counterB = new AtomicInteger();
    final String type = "foo";
    final String expectedBody = "HELLO WORLD!";
    final int totalMessages = 1000;

    DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
    engine.start();

    engine.addMessageListener(type, (Message<String> message) -> {
      counterA.incrementAndGet();
      lock.release();
    });
    engine.addMessageListener(type, (Message<String> message) -> {
      counterB.incrementAndGet();
      lock.release();
    });

    for (int i = 0; i < totalMessages; i++) {
      engine.send(new TestMessage<>(type, expectedBody));
    }

    lock.tryAcquire(totalMessages * 2, 3, TimeUnit.SECONDS);
    assertEquals(totalMessages, counterA.get());
    assertEquals(totalMessages, counterB.get());
    engine.stop();
  }

  @Test
  public void shouldStopListeningToMessage() throws Exception {
    final AtomicInteger counter = new AtomicInteger();
    final String type = "foo";
    final Semaphore lock = new Semaphore(0);

    DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
    engine.start();

    MessageListener<String> listener = engine.addMessageListener(type, (Message<String> message) -> {
      counter.incrementAndGet();
      lock.release();
    });

    engine.send(new TestMessage<>(type, "body"));
    lock.tryAcquire(1, TimeUnit.SECONDS);

    engine.removeMessageListener(type, listener);
    engine.send(new TestMessage<>(type, "body"));
    Thread.sleep(100);

    assertEquals(1, counter.get());
    engine.stop();
  }

  @Test
  public void shouldRequestAndBlockUntilResponse() throws Exception {
    final int timeout = 5000;
    final int responseDelay = 1000;
    final String responseType = "responseType";

    long startTime = System.currentTimeMillis();

    DefaultMessagingDelegate engine = new DefaultMessagingDelegate();
    engine.start();

    final Message<?> expectedResponse = new TestMessage<>(responseType, "responseBody");

    Future<Message<?>> future = engine.request(new TestMessage<>("requestType", "requestBody"), responseType, timeout);

    new Thread(() -> {
      try {
        Thread.sleep(responseDelay);
        engine.send(expectedResponse);
      } catch (InterruptedException e) {
      }
    }).start();

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

    Future<Message<?>> future = engine.request(new TestMessage<>("requestType", "requestBody"), "responseType", timeout);
    Message<?> actualResponse = future.get();

    long totalTime = System.currentTimeMillis() - startTime;
    assertTrue(totalTime >= timeout);
    assertNull(actualResponse);
    engine.stop();
  }

}
