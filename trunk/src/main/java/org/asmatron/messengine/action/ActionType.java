package org.asmatron.messengine.action;

public class ActionType<T extends ActionObject> {
	private final String id;

	public ActionType(String id) {
		this.id = id;
	}

	public Action<T> create(T param) {
		return new Action<T>(this, param);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		ActionType<?> other = (ActionType<?>) obj;
		return this.id.equals(other.id);
	}

	@Override
	public String toString() {
		return "ACTION:" + id;
	}

	public static final <T extends ActionObject> ActionType<T> cm(String id) {
		return new ActionType<T>(id);
	}
}
