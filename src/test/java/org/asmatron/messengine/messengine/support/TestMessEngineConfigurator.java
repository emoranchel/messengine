package org.asmatron.messengine.messengine.support;

import static org.junit.Assert.assertEquals;

import org.asmatron.messengine.messaging.MessEngine;
import org.asmatron.messengine.messaging.impl.StubMessEngine;
import org.asmatron.messengine.messaging.support.MessEngineConfigurator;
import org.junit.Test;
import org.mockito.Spy;


public class TestMessEngineConfigurator {
	@Spy
	private MessEngine messEngine = new StubMessEngine();
	private MessEngineConfigurator configurator = new MessEngineConfigurator(messEngine);

	@Test
	public void shouldManageMessage() throws Exception {
		MessageMethodTester annotationTester = new MessageMethodTester();
		configurator.setupMessEngine(annotationTester);
		messEngine.send(new SimpleMessage<String>(TestTypes.messageId, "dea"));
		assertEquals("dea", annotationTester.val);
	}

}
