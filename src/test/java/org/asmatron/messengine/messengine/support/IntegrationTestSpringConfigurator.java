package org.asmatron.messengine.messengine.support;

import static org.junit.Assert.assertEquals;

import org.asmatron.messengine.messaging.MessEngine;
import org.asmatron.messengine.messaging.impl.TestMessage;
import org.fest.swing.timing.Condition;
import org.fest.swing.timing.Pause;
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
		Pause.pause(new Condition("Waiting for the message to be process."){
			@Override
			public boolean test() {
				return testListener.getMessage()!=null;
			}
		});
		assertEquals("Hola Mundo", testListener.getMessage().getBody());
	}
	
}
