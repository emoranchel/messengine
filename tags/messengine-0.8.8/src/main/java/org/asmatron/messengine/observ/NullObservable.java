package org.asmatron.messengine.observ;

import java.awt.Component;

public class NullObservable<T extends ObserveObject> extends Observable<T> {

  private static final ObserverCollection<ObservValue<Component>> INSTANCE = new NullObservable<>();

  private NullObservable() {
  }

  @Override
  public boolean fire(T param) {
    return true;
  }

  @Override
  public void add(Observer<T> l) {
  }

  @Override
  public void remove(Observer<T> listener) {
  }

  @Override
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
