package org.asmatron.messengine.appControl.control;

import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionType;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;

public interface ActionDelegate {

	void send(Action<?> command);

	<V, T> void request(ActionType<RequestAction<V, T>> type, V requestParameter, ResponseCallback<T> callback);

	<T extends ActionObject> void addActionHandler(ActionType<T> actionType, ActionHandler<T> actionHandler);

	<T extends ActionObject> void removeActionHandler(ActionType<T> action);

	void start();

	void stop();

}
