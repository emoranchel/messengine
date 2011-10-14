package org.asmatron.messengine.app;

public interface ResultProcessor<T> {
	T result(Attributes attributes);
}
