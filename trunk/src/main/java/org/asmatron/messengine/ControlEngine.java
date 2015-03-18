package org.asmatron.messengine;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.event.EmptyEvent;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.model.ModelId;

/**
 * Engine for controllers and service objects.<br>
 * This engine allows service tier objects to communicate with the view.<br>
 *
 */
public interface ControlEngine extends EngineController {

  /**
   * Fires an event and allows all event listeners to handle it. <br>
   * This is a convenience method that fires an emptyEvent so eventListeners
   * does not need the event argument.
   *
   * @param eventId The id of the event (must be an EmptyEvent)
   */
  void fireEvent(EventId<EmptyEvent> eventId);

  /**
   * Fires the event with an argument passed to listeners.
   *
   * @param <T> The type of the argument, it must extend the EventObject class.
   * @param eventId The id of the event
   * @param argument The argument of matching T type that will be passed to
   * event listeners
   */
  <T extends EventObject> void fireEvent(EventId<T> eventId, T argument);

  /**
   * Convenience method that fires a value event (a wrapper of a simple class
   * into an eventObject).
   *
   * @param <T> Anything that we want to send to eventListeners.
   * @param eventId The id of the event (must be a ValueEvent)
   * @param argument the argument that will be wrapped on a valueEvent
   */
  <T> void fireValueEvent(EventId<ValueEvent<T>> eventId, T argument);

  /**
   * Gets a value from the engine model repository.
   *
   * @param <T> The type of the value stored
   * @param modelId the id of the model variable stored in the engine
   * @return the model variable value or null if none is found
   */
  <T> T get(ModelId<T> modelId);

  <T> void set(ModelId<T> modelId, T value, EventId<ValueEvent<T>> event);

  <T extends ActionObject> ActionHandler<T> addActionHandler(ActionId<T> actionId, ActionHandler<T> actionHandler);

  <T extends ActionObject> void removeActionHandler(ActionId<T> actionId);

}
