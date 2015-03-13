package org.asmatron.messengine.engines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.asmatron.messengine.event.Event;
import org.asmatron.messengine.event.EventExecutionMode;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.Listener;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultEventDelegate implements EventDelegate {

    private final static Logger log = Logger.getLogger(Event.class.getName());

    private Map pendingEvents = new HashMap();

    private Map<EventId<?>, EventCollection<?>> eventCollections = new HashMap<EventId<?>, EventCollection<?>>();
    private ExecutorService eventExecutor;

    public DefaultEventDelegate() {
        this(Executors.newCachedThreadPool());
    }

    public DefaultEventDelegate(ExecutorService eventExecutor) {
        this.eventExecutor = eventExecutor;
    }

    private <T extends EventObject> EventCollection<T> get(EventId<T> type) {
        EventCollection eventCollection = null;
        synchronized (this) {
            eventCollection = eventCollections.get(type);
            if (eventCollection == null) {
                eventCollection = new EventCollection<EventObject>(type);
                eventCollections.put(type, eventCollection);
            }
        }
        return eventCollection;
    }

    @Override
    public <T extends EventObject> void fireLater(EventId<T> type, T argument) {
        pendingEvents.put(type, argument);
    }

    public void start() {
        Set entrySet = pendingEvents.entrySet();
        for (Object o : entrySet) {
            Entry entry = (Entry) o;
            get((EventId) entry.getKey()).fire((EventObject) entry.getValue());
        }
        pendingEvents.clear();
    }

    public void stop() {
        eventExecutor.shutdown();
        synchronized (this) {
            for (Entry<EventId<?>, EventCollection<?>> entry : eventCollections.entrySet()) {
                entry.getValue().listeners.clear();
            }
            eventCollections.clear();
        }
    }

    @Override
    public <T extends EventObject> void removeListener(EventId<T> type, Listener<T> listener) {
        EventCollection<T> eventCollection = get(type);
        synchronized (eventCollection) {
            eventCollection.listeners.remove(listener);
        }
    }

    @Override
    public <T extends EventObject> void addListener(EventId<T> type, Listener<T> listener) {
        if (listener != null) {
            EventCollection<T> eventCollection = get(type);
            if (listener.isEager() && eventCollection.lastEvent != null) {
                EventExecutionMode mode = listener.getMode();
                if (mode == EventExecutionMode.ASYNC_IN_SWING || mode == EventExecutionMode.NORMAL_IN_SWING) {
                    inSwing(listener, eventCollection.lastEvent, true);
                } else {
                    async(listener, eventCollection.lastEvent);
                }
            }
            synchronized (eventCollection) {
                eventCollection.listeners.add(listener);
            }
        }
    }

    @Override
    public <T extends EventObject> void fireEvent(EventId<T> type, T argument) {
        get(type).fire(argument);
    }

    private <T extends EventObject> void async(final Listener<T> listener, final T arg) {
        eventExecutor.execute(new ListenerRunner<T>(listener, arg));
    }

    private <T extends EventObject> void inSwing(Listener<T> listener, T arg, boolean async) {
        if (async) {
            SwingUtilities.invokeLater(new ListenerRunner<T>(listener, arg));
        } else {
            try {
                SwingUtilities.invokeAndWait(new ListenerRunner<T>(listener, arg));
            } catch (Exception e) {
                throw new RuntimeException("Listener failed:", e);
            }
        }
    }

    class ListenerRunner<T extends EventObject> implements Runnable {

        private final Listener<T> listener;
        private final T arg;

        public ListenerRunner(Listener<T> listener, T arg) {
            this.listener = listener;
            this.arg = arg;

        }

        @Override
        public void run() {
            try {
                listener.handleEvent(arg);
            } catch (Throwable e) {
                log.log(Level.WARNING, "Handler " + listener.getClass().getName() + " FAILED!" + listener, e);
            }
        }
    }

    class EventCollection<T extends EventObject> {

        List<Listener<T>> listeners = new ArrayList<Listener<T>>();
        private T lastEvent;
        private final EventId<T> eventType;

        public EventCollection(EventId<?> eventType) {
            this.eventType = (EventId<T>) eventType;
        }

        public void fire(T param) {
            List<Listener<T>> listeners = null;
            synchronized (this) {
                listeners = new ArrayList<Listener<T>>(this.listeners);
            }
            if (listeners.isEmpty()) {
                log.log(Level.WARNING, "Event " + eventType + " is not handled by anyone, though not an error this should be avoided!");
            }
            for (Listener<T> listener : listeners) {
                try {
                    switch (listener.getMode()) {
                        case ASYNC:
                            async(listener, param);
                            break;
                        case ASYNC_IN_SWING:
                            inSwing(listener, param, true);
                            break;
                        case NORMAL_IN_SWING:
                            inSwing(listener, param, false);
                        default:
                            listener.handleEvent(param);
                            break;
                    }
                } catch (Throwable e) {
                    log.log(Level.WARNING, "Handler " + listener.getClass().getName() + " FAILED!" + listener, e);
                }
            }
            lastEvent = param;
        }
    }

}
