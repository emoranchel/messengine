package org.asmatron.messengine.appControl;

import org.asmatron.messengine.action.ResponseCallback;

@SuppressWarnings("unchecked")
public class EmptyCallback<T> implements ResponseCallback<T> {

	private static final ResponseCallback<Integer> INSTANCE = new EmptyCallback();

	@Override
	public void onResponse(T t) {
	}

	public static <T> ResponseCallback<T> get(Class<T> t) {
		return (ResponseCallback<T>) INSTANCE;
	}

}
