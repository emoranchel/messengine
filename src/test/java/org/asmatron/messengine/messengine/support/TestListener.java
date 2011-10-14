package org.asmatron.messengine.messengine.support;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.messaging.support.MessageSelector;
import org.junit.Test;


@MessageSelector("foo")
public class TestListener implements MessageListener<Message<String>> {
	AtomicInteger counter = new AtomicInteger(0);

	private Message<String> message;

	@Override
	public void onMessage(Message<String> message) {
		this.message = message;
		counter.incrementAndGet();
	}

	public Message<String> getMessage() {
		return message;
	}
	
	@Test
	public void shouldHaveAtLeastOneTestToAllowHudsonBuild() throws Exception {
		assertTrue(true);
	}

}
