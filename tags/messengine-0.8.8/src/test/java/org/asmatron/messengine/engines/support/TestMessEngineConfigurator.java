package org.asmatron.messengine.engines.support;

import static org.junit.Assert.assertEquals;

import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.messaging.SimpleMessage;
import org.asmatron.messengine.testing.TestEngine;
import org.junit.Test;
import org.mockito.Spy;

public class TestMessEngineConfigurator {

  @Spy
  private final MessEngine messEngine = new TestEngine();
  private final MessagingConfigurator configurator = new MessagingConfigurator(messEngine);

  @Test
  public void shouldManageMessage() throws Exception {
    MessageMethodTester annotationTester = new MessageMethodTester();
    configurator.setupMessEngine(annotationTester);
    messEngine.send(new SimpleMessage<>(TestTypes.messageId, "dea"));
    assertEquals("dea", annotationTester.val);
  }

}
