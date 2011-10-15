package org.asmatron.messengine.engines;

import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.Listener;

public interface EventDelegate extends BaseDelegate {

	<T extends EventObject> void removeListener(EventId<T> type, Listener<T> listener);

	<T extends EventObject> void addListener(EventId<T> type, Listener<T> listener);

	<T extends EventObject> void fireEvent(EventId<T> type, T argument);

	<T extends EventObject> void fireLater(EventId<T> type, T argument);

}
