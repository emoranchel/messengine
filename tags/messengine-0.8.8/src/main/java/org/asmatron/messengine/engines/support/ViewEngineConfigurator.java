package org.asmatron.messengine.engines.support;

import java.lang.reflect.Method;
import java.util.List;

import org.asmatron.messengine.ViewEngine;
import org.asmatron.messengine.annotations.EventMethod;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.util.AppAnnotationUtils;

public class ViewEngineConfigurator {

  private ViewEngine viewEngine;

  public ViewEngineConfigurator() {
  }

  public ViewEngineConfigurator(ViewEngine viewEngine) {
    setViewEngine(viewEngine);
  }

  public int setupViewEngine(Object object) {
    List<Method> eventMethods = AppAnnotationUtils.getMethods(object.getClass(), EventMethod.class);
    if (eventMethods.isEmpty()) {
      return 0;
    }
    eventMethods.stream().forEach((method) -> {
      addEventMethodHandler(object, method);
    });
    return eventMethods.size();
  }

  public int resetViewEngine(Object object) {
    List<Method> eventMethods = AppAnnotationUtils.getMethods(object.getClass(), EventMethod.class);
    if (eventMethods.isEmpty()) {
      return 0;
    }
    eventMethods.stream().forEach((method) -> {
      removeEventMethodHandler(object, method);
    });
    return eventMethods.size();
  }

  private void addEventMethodHandler(Object object, Method method) {
    checkViewEngine();
    EventMethod annotation = method.getAnnotation(EventMethod.class);
    String id = annotation.value();
    EventId<EventObject> eventType = EventId.ev(id);
    EventMethodListener listener = new EventMethodListener(object, method, annotation.mode(), annotation.eager());
    viewEngine.addListener(eventType, listener);
  }

  private void removeEventMethodHandler(Object object, Method method) {
    checkViewEngine();
    EventMethod annotation = method.getAnnotation(EventMethod.class);
    String id = annotation.value();
    EventId<EventObject> eventType = EventId.ev(id);
    EventMethodListener listener = new EventMethodListener(object, method, annotation.mode(), annotation.eager());
    viewEngine.removeListener(eventType, listener);
  }

  private void checkViewEngine() {
    if (viewEngine == null) {
      throw new IllegalStateException("Autoconfigure failed no viewEngine set.");
    }
  }

  public ViewEngine getViewEngine() {
    return viewEngine;
  }

  public final void setViewEngine(ViewEngine viewEngine) {
    this.viewEngine = viewEngine;
  }
}
