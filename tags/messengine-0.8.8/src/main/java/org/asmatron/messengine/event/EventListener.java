package org.asmatron.messengine.event;

import org.asmatron.messengine.Handler;

public interface EventListener<T extends EventObject> {

  void handleEvent(T eventArgs);

  default EventExecutionMode getMode() {
    return EventExecutionMode.NORMAL;
  }

  default boolean isEager() {
    return false;
  }

  static <T extends EventObject> EventListener<T> newEventListener(boolean eager, Handler<T> handler) {
    return newEventListener(EventExecutionMode.NORMAL, eager, handler);
  }

  static <T extends EventObject> EventListener<T> newEventListener(EventExecutionMode mode, Handler<T> handler) {
    return newEventListener(mode, false, handler);
  }

  static <T extends EventObject> EventListener<T> newEventListener(EventExecutionMode mode, boolean eager, Handler<T> handler) {
    return new EventListener<T>() {
      @Override
      public boolean isEager() {
        return eager;
      }

      @Override
      public EventExecutionMode getMode() {
        return mode;
      }

      @Override
      public void handleEvent(T eventArgs) {
        handler.handle(eventArgs);
      }
    };
  }
}
