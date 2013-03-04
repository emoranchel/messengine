package org.asmatron.messengine.engines.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.asmatron.messengine.EngineController;
import org.asmatron.messengine.EngineListener;
import org.asmatron.messengine.annotations.EngineStart;
import org.asmatron.messengine.annotations.EngineStop;

public class EngineListenerConfigurator {
  private EngineController engineController;

  public EngineListenerConfigurator(EngineController engineController) {
    this.engineController = engineController;
  }

  public void setupListeners(Object object) {
    if(engineController==null){
      throw new IllegalStateException("Autoconfigure failed no messEngine set.");
    }
    if (object instanceof EngineListener) {
      engineController.addEngineListener((EngineListener) object);
    }
    for (Method method : getMethods(object.getClass(), EngineStart.class)){
      engineController.addEngineListener(new StartEngineMethodHandler(object, method));
    }
    for(Method method : getMethods(object.getClass(), EngineStop.class)){
      engineController.addEngineListener(new StopEngineMethodHandler(object, method));
    } 
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
    List<Method> methods = new ArrayList<Method>();
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
