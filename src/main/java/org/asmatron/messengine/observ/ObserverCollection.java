package org.asmatron.messengine.observ;

public interface ObserverCollection<T extends ObserveObject> {

  public void add(Observer<T> l);

  public void remove(Observer<T> listener);

}
