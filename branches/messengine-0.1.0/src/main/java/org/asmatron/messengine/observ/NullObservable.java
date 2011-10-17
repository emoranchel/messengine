package org.asmatron.messengine.observ;

import java.awt.Component;

public class NullObservable<T extends ObserveObject> extends Observable<T> {
	private static final ObserverCollection<ObservValue<Component>> INSTANCE = new NullObservable<ObservValue<Component>>();

	private NullObservable() {
	}

	public boolean fire(T param) {
		return true;
	}

	public void add(Observer<T> l) {
	}

	public void remove(Observer<T> listener) {
	}

	public void clean() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends ObserveObject> Observable<T> get() {
		return (Observable<T>) INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ObserveObject> ObserverCollection<T> getCollection() {
		return (ObserverCollection<T>) INSTANCE;
	}

}
