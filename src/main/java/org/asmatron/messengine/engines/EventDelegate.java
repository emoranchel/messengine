package org.asmatron.messengine.engines;

import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventType;
import org.asmatron.messengine.event.Listener;

public interface EventDelegate extends BaseDelegate {

	<T extends EventObject> void removeListener(EventType<T> type, Listener<T> listener);

	<T extends EventObject> void addListener(EventType<T> type, Listener<T> listener);

	<T extends EventObject> void fireEvent(EventType<T> type, T argument);

	<T extends EventObject> void fireLater(EventType<T> type, T argument);

}
