package org.asmatron.messengine.engines;

import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.EventListener;

public interface EventDelegate extends BaseDelegate {

  <T extends EventObject> void removeListener(EventId<T> type, EventListener<T> listener);

  <T extends EventObject> void addListener(EventId<T> type, EventListener<T> listener);

  <T extends EventObject> void fireEvent(EventId<T> type, T argument);

}
