package org.asmatron.messengine.event;

public class EventType<T extends EventObject> {
	private final String id;

	public EventType(String id) {
		this.id = id;
	}

	public Event<T> create(T arg) {
		return new Event<T>(this, arg);
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
		EventType<?> other = (EventType<?>) obj;
		return this.id.equals(other.id);
	}

	public static final <T extends EventObject> EventType<T> ev(String id) {
		return new EventType<T>(id);
	}

}
