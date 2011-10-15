package org.asmatron.messengine;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionType;
import org.asmatron.messengine.event.EmptyEvent;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventType;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.model.ModelType;

public interface ControlEngine extends EngineController {
	void fireEvent(EventType<EmptyEvent> type);

	<T extends EventObject> void fireEvent(EventType<T> type, T argument);

	<T> void fireValueEvent(EventType<ValueEvent<T>> type, T argument);

	<T> T get(ModelType<T> type);

	<T> void set(ModelType<T> type, T value, EventType<ValueEvent<T>> event);

	<T extends ActionObject> void addActionHandler(ActionType<T> actionType, ActionHandler<T> actionHandler);

	<T extends ActionObject> void removeActionHandler(ActionType<T> action);

}
