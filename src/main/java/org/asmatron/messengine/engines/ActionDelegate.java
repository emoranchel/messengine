package org.asmatron.messengine.engines;

import org.asmatron.messengine.action.Action;
import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;

public interface ActionDelegate extends BaseDelegate {

  void send(Action<?> command);

  <V, T> void request(ActionId<RequestAction<V, T>> type, V requestParameter, ResponseCallback<T> callback);

  <T extends ActionObject> void addActionHandler(ActionId<T> actionType, ActionHandler<T> actionHandler);

  <T extends ActionObject> void removeActionHandler(ActionId<T> action);

}
