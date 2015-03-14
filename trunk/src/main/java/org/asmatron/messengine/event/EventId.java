package org.asmatron.messengine.event;

public class EventId<T extends EventObject> {

  private final String id;

  public EventId(String id) {
    this.id = id;
  }

  public Event<T> create(T arg) {
    return new Event<>(this, arg);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "EVENT:" + id;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EventId) {
      EventId<?> other = (EventId<?>) obj;
      return this.id.equals(other.id);
    }
    return false;
  }

  public static final <T extends EventObject> EventId<T> ev(String id) {
    return new EventId<>(id);
  }

}
