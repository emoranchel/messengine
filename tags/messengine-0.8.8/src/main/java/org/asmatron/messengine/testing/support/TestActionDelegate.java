package org.asmatron.messengine.testing.support;

import org.asmatron.messengine.engines.DefaultActionDelegate;

public class TestActionDelegate extends DefaultActionDelegate {

  public TestActionDelegate() {
    super(new TestingExecutorService(), new TestActionRunnerFactory());
  }
}
