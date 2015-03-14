package org.asmatron.messengine.engines.components;

import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionObject;

public interface ActionRunnerFactory {

  public <T extends ActionObject> ActionRunnable createRunner(ActionProcessor<T> commandProcessor, Action<T> command);
}
