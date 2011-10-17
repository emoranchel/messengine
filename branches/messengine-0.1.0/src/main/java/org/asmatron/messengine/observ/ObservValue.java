package org.asmatron.messengine.observ;

public class ObservValue<T> extends ObserveObject {
	private final T value;

	public ObservValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
}
