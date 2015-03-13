package org.asmatron.messengine.engines;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;
import org.asmatron.messengine.engines.components.ActionProcessor;
import org.asmatron.messengine.engines.components.ActionRunnerFactory;
import org.asmatron.messengine.engines.components.ActionThreadFactory;
import org.asmatron.messengine.engines.components.DefaultActionRunnerFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultActionDelegate implements ActionDelegate {
	private Map<ActionId<?>, ActionProcessor<?>> actionCollections = new HashMap<ActionId<?>, ActionProcessor<?>>();
	private ExecutorService actionExecutor;
	private ActionRunnerFactory runnerFactory;

	public DefaultActionDelegate() {
		this(Executors.newCachedThreadPool(new ActionThreadFactory()), new DefaultActionRunnerFactory());
	}

	protected DefaultActionDelegate(ExecutorService executorService, ActionRunnerFactory runnerFactory) {
		actionExecutor = executorService;
		this.runnerFactory = runnerFactory;
	}

	public <T extends ActionObject> void addActionHandler(ActionId<T> command, ActionHandler<T> commandHandler) {
		ActionProcessor<T> commandProcessor = get(command, true);
		commandProcessor.handle(commandHandler);
	}

	public void send(Action<?> command) {
		ActionProcessor commandProcessor = get(command.getType(), false);
		actionExecutor.submit(runnerFactory.createRunner(commandProcessor, command));
	}

	public <T extends ActionObject> ActionProcessor<T> get(ActionId<T> command, boolean create) {
		ActionProcessor<T> handler = (ActionProcessor<T>) actionCollections.get(command);
		if (handler == null && create) {
			handler = new ActionProcessor<T>(command);
			actionCollections.put(command, handler);
		}
		return handler;
	}

	public <T extends ActionObject> void removeActionHandler(ActionId<T> command) {
		actionCollections.remove(command);
	}

	public void start() {
	}

	public void stop() {
		actionExecutor.shutdown();
		synchronized (this) {
			for (Entry<ActionId<?>, ActionProcessor<?>> entry : actionCollections.entrySet()) {
				entry.getValue().clean();
			}
			actionCollections.clear();
		}
	}

	public <V, T> void request(ActionId<RequestAction<V, T>> type, V requestParameter, ResponseCallback<T> callback) {
		RequestAction<V, T> request = new RequestAction<V, T>(requestParameter, callback);
		Action<RequestAction<V, T>> action = type.create(request);
		send(action);
	}

}
