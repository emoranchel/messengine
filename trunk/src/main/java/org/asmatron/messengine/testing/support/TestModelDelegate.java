package org.asmatron.messengine.testing.support;

import java.util.HashMap;
import java.util.Map;

import org.asmatron.messengine.engines.DefaultModelDelegate;
import org.asmatron.messengine.model.ModelType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TestModelDelegate extends DefaultModelDelegate {
	private Map model;

	public TestModelDelegate() {
		model = new HashMap();
	}

	@Override
	public <T> T get(ModelType<T> type) {
		T value = (T) model.get(type);
		return value;
	}

	@Override
	public <T> void set(ModelType<T> type, T value) {
		model.put(type, value);
	}

	@Override
	public void start() {
	}

	@Override
	public synchronized void stop() {
	}

}
