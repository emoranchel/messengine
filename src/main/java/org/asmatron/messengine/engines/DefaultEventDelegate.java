package org.asmatron.messengine.engines;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.asmatron.messengine.event.Event;
import org.asmatron.messengine.event.EventExecutionMode;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.EventListener;

public class DefaultEventDelegate implements EventDelegate {

  private final static Logger log = Logger.getLogger(Event.class.getName());

  private final Map<EventId<?>, EventCollection<?>> eventCollections = new HashMap<>();
  private ExecutorService eventExecutor;

  public DefaultEventDelegate() {
    this(Executors.newCachedThreadPool());
  }

  public DefaultEventDelegate(ExecutorService eventExecutor) {
    this.eventExecutor = eventExecutor;
  }

  private <T extends EventObject> EventCollection<T> get(EventId<T> type) {
    EventCollection eventCollection;
    synchronized (this) {
      eventCollection = eventCollections.get(type);
      if (eventCollection == null) {
        eventCollection = new EventCollection<>(type);
        eventCollections.put(type, eventCollection);
      }
    }
    return eventCollection;
  }

  @Override
  public void start() {
  }

  @Override
  public void stop() {
    eventExecutor.shutdown();
    synchronized (this) {
      eventCollections.values().stream().forEach((event) -> {
        event.listeners.clear();
      });
      eventCollections.clear();
    }
  }

  @Override
  public <T extends EventObject> void removeListener(EventId<T> type, EventListener<T> listener) {
    EventCollection<T> eventCollection = get(type);
    synchronized (eventCollection) {
      eventCollection.listeners.remove(listener);
    }
  }

  @Override
  public <T extends EventObject> void addListener(EventId<T> type, EventListener<T> listener) {
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

  private <T extends EventObject> void async(final EventListener<T> listener, final T arg) {
    eventExecutor.execute(new ListenerRunner<>(listener, arg));
  }

  private <T extends EventObject> void inSwing(EventListener<T> listener, T arg, boolean async) {
    if (async) {
      SwingUtilities.invokeLater(new ListenerRunner<>(listener, arg));
    } else {
      try {
        SwingUtilities.invokeAndWait(new ListenerRunner<>(listener, arg));
      } catch (Exception e) {
        throw new RuntimeException("Listener failed:", e);
      }
    }
  }

  class ListenerRunner<T extends EventObject> implements Runnable {

    private final EventListener<T> listener;
    private final T arg;

    public ListenerRunner(EventListener<T> listener, T arg) {
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

    List<EventListener<T>> listeners = new ArrayList<>();
    private T lastEvent;
    private final EventId<T> eventType;

    public EventCollection(EventId<?> eventType) {
      this.eventType = (EventId<T>) eventType;
    }

    public void fire(T param) {
      List<EventListener<T>> listenersCopy;
      synchronized (this) {
        listenersCopy = new ArrayList<>(this.listeners);
      }
      if (listenersCopy.isEmpty()) {
        log.log(Level.WARNING, "Event {0} is not handled by anyone, though not an error this should be avoided!", eventType);
      }
      for (EventListener<T> listener : listenersCopy) {
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
