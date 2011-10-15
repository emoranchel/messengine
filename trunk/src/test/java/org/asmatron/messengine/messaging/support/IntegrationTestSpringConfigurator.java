package org.asmatron.messengine.messaging.support;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.asmatron.messengine.messaging.MessEngine;
import org.asmatron.messengine.messaging.impl.TestMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test.xml")
public class IntegrationTestSpringConfigurator {
	@Autowired
	MessEngine engine;
	@Autowired
	TestListener testListener;
	@Test
	public void shouldListenerFooMessage() throws Exception {
		engine.send(new TestMessage<String>("foo", "Hola Mundo"));
		testListener.lock.tryAcquire(1, 3, TimeUnit.SECONDS);
		assertEquals("Hola Mundo", testListener.getMessage().getBody());
	}
	
}
