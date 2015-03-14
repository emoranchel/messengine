package org.asmatron.messengine;

public interface Handler<T> {

  public void handle(T t);
}
