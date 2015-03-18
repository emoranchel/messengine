package org.asmatron.messengine.engines.support;

import org.asmatron.messengine.annotations.MessageMethod;
import org.asmatron.messengine.messaging.Message;

public class MessageMethodTester {

  public String val = null;

  @MessageMethod(TestTypes.messageId)
  public void handleEvent(Message<String> valueEvent) {
    val = valueEvent.getBody();
  }

}
