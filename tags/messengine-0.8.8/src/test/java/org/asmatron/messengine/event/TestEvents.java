package org.asmatron.messengine.event;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.asmatron.messengine.engines.DefaultEventDelegate;
import org.junit.Test;

public class TestEvents {

  private static final EventId<CustomEvent> aType = new EventId<>("a");

  @Test
  public void shouldTestEventEngine() throws Exception {
    final AtomicBoolean invoked = new AtomicBoolean(false);

    DefaultEventDelegate engine = new DefaultEventDelegate();

    engine.addListener(aType, (CustomEvent eventArgs) -> {
      eventArgs.hashCode();
      invoked.set(true);
    });

    engine.fireEvent(aType, new CustomEvent());
    assertTrue(invoked.get());
  }
}

class CustomEvent extends EventObject {
}
