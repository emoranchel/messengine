package org.asmatron.messengine.testing.support;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.NoHandlerException;
import org.asmatron.messengine.engines.components.ActionFailedToExecute;
import org.asmatron.messengine.engines.components.ActionProcessor;
import org.asmatron.messengine.engines.components.ActionRunnable;
import org.asmatron.messengine.engines.components.ActionRunnerFactory;

public class TestActionRunnerFactory implements ActionRunnerFactory {

  @Override
  public <T extends ActionObject> ActionRunnable createRunner(ActionProcessor<T> commandProcessor, Action<T> command) {
    return new ActionRunner<>(commandProcessor, command, Thread.currentThread().getStackTrace());
  }

  private final static Logger log = Logger.getLogger(ActionRunner.class.getName());

  class ActionRunner<T extends ActionObject> implements ActionRunnable {

    private final ActionProcessor<T> commandProcessor;
    private final Action<T> command;
    private final StackTraceElement[] stack;

    public ActionRunner(ActionProcessor<T> commandProcessor, Action<T> command, StackTraceElement[] stack) {
      this.commandProcessor = commandProcessor;
      this.command = command;
      this.stack = stack;
    }

    @Override
    public void run() {
      T param = command.getParam();
      try {
        if (commandProcessor == null) {
          throw new NoHandlerException("No handler found for action: " + command.getType().getId());
        } else {
          commandProcessor.fire(param);
        }
      } catch (Exception e) {
        ActionFailedToExecute actionFailedToExecute = new ActionFailedToExecute(e, stack);
        log.log(Level.SEVERE, "ACTION " + command.getType() + " FAILED!", actionFailedToExecute);
        throw actionFailedToExecute;
      }
    }

    @Override
    public String getName() {
      return command.getType().getId();
    }
  }

}
