package org.asmatron.messengine.engines.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.NoHandlerException;


public class DefaultActionRunnerFactory implements ActionRunnerFactory {
	@Override
	public <T extends ActionObject> ActionRunnable createRunner(ActionProcessor<T> commandProcessor, Action<T> command) {
		return new ActionRunner<T>(commandProcessor, command, Thread.currentThread().getStackTrace());
	}

	private final static Log log = LogFactory.getLog(ActionRunner.class);

	class ActionRunner<T extends ActionObject> implements ActionRunnable {

		private final ActionProcessor<T> commandProcessor;
		private final Action<T> command;
		private final StackTraceElement[] stack;

		public ActionRunner(ActionProcessor<T> commandProcessor, Action<T> command, StackTraceElement[] stack) {
			this.commandProcessor = commandProcessor;
			this.command = command;
			this.stack = stack;
		}

		public void run() {
			Thread.currentThread().setName("ACTIONTHREAD:" + command.getType().getId());
			T param = command.getParam();
			try {
				if (commandProcessor == null) {
					throw new NoHandlerException("No handler found for action: " + command.getType().getId());
				} else {
					commandProcessor.fire(param);
				}
			} catch (Exception e) {
				ActionFailedToExecute actionFailedToExecute = new ActionFailedToExecute(e, stack);
				log.error("ACTION " + command.getType() + " FAILED!", actionFailedToExecute);
				throw actionFailedToExecute;
			}
		}

		@Override
		public String getName() {
			return command.getType().getId();
		}
	}

}
