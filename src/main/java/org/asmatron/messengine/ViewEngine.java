package org.asmatron.messengine;

import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionType;
import org.asmatron.messengine.action.EmptyAction;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventType;
import org.asmatron.messengine.event.Listener;
import org.asmatron.messengine.model.ModelType;

public interface ViewEngine {
	<T extends ActionObject> void send(ActionType<T> actionType, T parameter);

	<T> void sendValueAction(ActionType<ValueAction<T>> action, T argument);

	void send(ActionType<EmptyAction> action);

	<V, T> void request(ActionType<RequestAction<V, T>> type, V requestParameter, ResponseCallback<T> callback);

	<T> void request(ActionType<RequestAction<Void, T>> type, ResponseCallback<T> callback);

	<T> T get(ModelType<T> type);

	<T extends EventObject> void removeListener(EventType<T> currentviewchanged, Listener<T> listener);

	<T extends EventObject> void addListener(EventType<T> currentviewchanged, Listener<T> listener);

}
