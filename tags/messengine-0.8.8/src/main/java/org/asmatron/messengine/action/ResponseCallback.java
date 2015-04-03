package org.asmatron.messengine.action;

public interface ResponseCallback<T> {

  void onResponse(T t);
}
