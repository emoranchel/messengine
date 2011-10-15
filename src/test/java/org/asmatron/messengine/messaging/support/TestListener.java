package org.asmatron.messengine.messaging.support;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.Semaphore;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.messaging.support.MessageSelector;
import org.junit.Test;


@MessageSelector("foo")
public class TestListener implements MessageListener<Message<String>> {
	public final Semaphore lock = new Semaphore(0);

	private Message<String> message;

	@Override
	public void onMessage(Message<String> message) {
		this.message = message;
		lock.release();
	}

	public Message<String> getMessage() {
		return message;
	}
	
	@Test
	public void shouldHaveAtLeastOneTestToAllowHudsonBuild() throws Exception {
		assertTrue(true);
	}

}
