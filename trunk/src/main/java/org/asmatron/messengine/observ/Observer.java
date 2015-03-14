package org.asmatron.messengine.observ;

public interface Observer<T extends ObserveObject> {

  public void observe(T t);

}
