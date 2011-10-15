package org.asmatron.messengine.engines;

import org.asmatron.messengine.model.ModelType;

public interface ModelDelegate extends BaseDelegate {
	<T> T get(ModelType<T> type);

	<T> T forceGet(ModelType<T> type);

	<T> void set(ModelType<T> type, T value);

}
