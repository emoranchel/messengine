package org.asmatron.messengine.appControl.control;

import org.asmatron.messengine.model.ModelType;

public interface ModelDelegate {
	<T> T get(ModelType<T> type);

	<T> T forceGet(ModelType<T> type);

	<T> void set(ModelType<T> type, T value);

	void start();

	void stop();
}
