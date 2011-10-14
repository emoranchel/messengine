package org.asmatron.messengine.event;

public class Event<T extends EventObject> {
	private final EventType<T> type;
	private final T param;

	public Event(EventType<T> type, T param) {
		this.type = type;
		this.param = param;
	}

	public EventType<T> getType() {
		return type;
	}

	public T getParam() {
		return param;
	}

	@Override
	public String toString() {
		return type.toString() + "[" + param.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((param == null) ? 0 : param.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Event<?> other = (Event<?>) obj;
		if (param == null) {
			if (other.param != null) {
				return false;
			}
		} else if (!param.equals(other.param)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

}