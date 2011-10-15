package org.asmatron.messengine;

import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.EmptyAction;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.Listener;
import org.asmatron.messengine.model.ModelId;

public interface ViewEngine {
	<T extends ActionObject> void send(ActionId<T> actionType, T parameter);

	<T> void sendValueAction(ActionId<ValueAction<T>> action, T argument);

	void send(ActionId<EmptyAction> action);

	<V, T> void request(ActionId<RequestAction<V, T>> type, V requestParameter, ResponseCallback<T> callback);

	<T> void request(ActionId<RequestAction<Void, T>> type, ResponseCallback<T> callback);

	<T> T get(ModelId<T> type);

	<T extends EventObject> void removeListener(EventId<T> currentviewchanged, Listener<T> listener);

	<T extends EventObject> void addListener(EventId<T> currentviewchanged, Listener<T> listener);

}
