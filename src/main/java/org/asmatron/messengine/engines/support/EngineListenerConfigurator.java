package org.asmatron.messengine.engines.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.asmatron.messengine.EngineController;
import org.asmatron.messengine.EngineListener;
import org.asmatron.messengine.annotations.EngineStarted;
import org.asmatron.messengine.annotations.EngineStarting;
import org.asmatron.messengine.annotations.EngineStoped;
import org.asmatron.messengine.annotations.EngineStoping;

public class EngineListenerConfigurator {

  private EngineController engineController;

  public EngineListenerConfigurator(EngineController engineController) {
    this.engineController = engineController;
  }

  public void setupListeners(Object object) {
    if (engineController == null) {
      throw new IllegalStateException("Autoconfigure failed no messEngine set.");
    }
    if (object instanceof EngineListener) {
      engineController.addEngineListener((EngineListener) object);
    }
    getMethods(object.getClass(), EngineStarting.class).stream().forEach((method) -> {
      engineController.addEngineListener(new EngineListenerMethodHandler(object, method, EngineListenerMethodHandler.Mode.STARTING));
    });
    getMethods(object.getClass(), EngineStoping.class).stream().forEach((method) -> {
      engineController.addEngineListener(new EngineListenerMethodHandler(object, method, EngineListenerMethodHandler.Mode.STOPING));
    });
    getMethods(object.getClass(), EngineStarted.class).stream().forEach((method) -> {
      engineController.addEngineListener(new EngineListenerMethodHandler(object, method, EngineListenerMethodHandler.Mode.STARTED));
    });
    getMethods(object.getClass(), EngineStoped.class).stream().forEach((method) -> {
      engineController.addEngineListener(new EngineListenerMethodHandler(object, method, EngineListenerMethodHandler.Mode.STOPED));
    });
  }

  public void resetListeners(Object object) {
    if (object instanceof EngineListener) {
      engineController.removeEngineListener((EngineListener) object);
    }
  }

  public EngineController getEngineController() {
    return engineController;
  }

  public void setEngineController(EngineController engineController) {
    this.engineController = engineController;
  }

  private List<Method> getMethods(Class<?> objectClass,
          Class<? extends Annotation> annotationClass) {
    List<Method> methods = new ArrayList<>();
    getMethods(objectClass, annotationClass, methods);
    return methods;
  }

  private void getMethods(Class<?> objectClass,
          Class<? extends Annotation> annotationClass, List<Method> methods) {
    if (objectClass != null) {
      Method[] declaredMethods = objectClass.getDeclaredMethods();
      for (Method method : declaredMethods) {
        if (method.isAnnotationPresent(annotationClass)) {
          methods.add(method);
        }
      }
      getMethods(objectClass.getSuperclass(), annotationClass, methods);
    }
  }

}
