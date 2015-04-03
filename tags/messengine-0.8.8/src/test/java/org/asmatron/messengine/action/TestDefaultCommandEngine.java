package org.asmatron.messengine.action;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.asmatron.messengine.engines.DefaultActionDelegate;
import org.asmatron.messengine.testing.support.TestActionDelegate;
import org.junit.Test;

public class TestDefaultCommandEngine {

  private static final ActionId<DemoCommandObject> cType = new ActionId<>("c");

  @Test
  public void shouldAddAndRunAnAction() throws Exception {
    DefaultActionDelegate engine = new TestActionDelegate();
    final AtomicBoolean handled = new AtomicBoolean(false);

    engine.addActionHandler(cType, (DemoCommandObject arg) -> {
      handled.set(true);
    });
    engine.send(cType.create(new DemoCommandObject()));
    assertTrue(handled.get());
  }

  @Test(expected = DuplicateActionHandlerException.class)
  public void shouldNotAddTwoHandlers() throws Exception {
    DefaultActionDelegate engine = new TestActionDelegate();
    engine.addActionHandler(cType, (DemoCommandObject arg) -> {
    });
    engine.addActionHandler(cType, (DemoCommandObject arg) -> {
    });
  }
}

class DemoCommandObject extends ActionObject {
}
