package org.asmatron.messengine.appControl.control;

import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionProcessor;

public interface ActionRunnerFactory {
	public <T extends ActionObject> ActionRunnable createRunner(ActionProcessor<T> commandProcessor, Action<T> command);

}
