package org.asmatron.messengine.app;

public interface Application<T> {
	void addAppListener(AppListener listener);

	void setResultProcessor(ResultProcessor<T> resultProcessor);

	T execute();
}
