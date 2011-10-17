package org.asmatron.messengine.action;

public class RequestAction<T, V> extends ActionObject {
	private final T value;
	private final ResponseCallback<V> callback;

	public RequestAction(T value, ResponseCallback<V> callback) {
		this.value = value;
		this.callback = callback;
	}

	public T getValue() {
		return value;
	}

	public ResponseCallback<V> getCallback() {
		return callback;
	}

}
