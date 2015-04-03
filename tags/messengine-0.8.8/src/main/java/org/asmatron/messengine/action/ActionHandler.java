package org.asmatron.messengine.action;

public interface ActionHandler<T extends ActionObject> {

  void handle(T arg);
}
