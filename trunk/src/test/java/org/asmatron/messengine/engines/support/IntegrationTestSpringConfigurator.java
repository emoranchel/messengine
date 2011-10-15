package org.asmatron.messengine.engines.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.asmatron.messengine.EngineController;
import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.engines.support.spring.SpringAppControlAutoConfigurerBeanPostProcessor;
import org.asmatron.messengine.testing.TestMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test.xml")
public class IntegrationTestSpringConfigurator {
	@Resource
	MessEngine engine;
	@Resource
	TestListener testListener;
	@Resource
	SpringAppControlAutoConfigurerBeanPostProcessor conf;
	@Resource
	EngineController controller;

	@Before
	public void setup() {
		controller.start();
	}

	@After
	public void teardown() {
		controller.stop();
	}

	@Test
	public void shouldListenerFooMessage() throws Exception {
		assertNotNull(engine);
		assertNotNull(testListener);
		assertNotNull(conf);
		engine.send(new TestMessage<String>("foo", "Hola Mundo"));
		testListener.lock.tryAcquire(1, 3, TimeUnit.SECONDS);
		assertEquals("Hola Mundo", testListener.getMessage().getBody());
	}

}
