package org.asmatron.messengine.engines.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.annotations.MessageMethod;
import org.asmatron.messengine.annotations.MessageListenerClass;
import org.asmatron.messengine.messaging.MessageListener;

public class MessagingConfigurator {

  private MessEngine messEngine;

  public MessagingConfigurator() {
  }

  public MessagingConfigurator(MessEngine messEngine) {
    setMessEngine(messEngine);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public int setupMessEngine(Object object) {
    int count = 0;
    MessageListenerClass selector = findAnnotation(object.getClass(), MessageListenerClass.class);
    if (selector != null && object instanceof MessageListener) {
      MessageListener listener = (MessageListener) object;
      messEngine.addMessageListener(selector.value(), listener);
      count++;
    }
    List<Method> messageMethods = getMethods(object.getClass(), MessageMethod.class);
    if (messageMethods.isEmpty()) {
      return count;
    }
    messageMethods.stream().forEach((method) -> {
      addMessageMethodHandler(object, method);
    });
    return messageMethods.size() + count;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public int resetMessEngine(Object object) {
    int count = 0;
    MessageListenerClass selector = findAnnotation(object.getClass(), MessageListenerClass.class);
    if (selector != null && object instanceof MessageListener) {
      MessageListener listener = (MessageListener) object;
      messEngine.removeMessageListener(selector.value(), listener);
      count++;
    }
    List<Method> messageMethods = getMethods(object.getClass(), MessageMethod.class);
    if (messageMethods.isEmpty()) {
      return count;
    }
    messageMethods.stream().forEach((method) -> {
      removeMessageMethodHandler(object, method);
    });
    return messageMethods.size() + count;
  }

  private void addMessageMethodHandler(Object object, Method method) {
    checkMessEngine();
    MessageMethod annotation = method.getAnnotation(MessageMethod.class);
    MessageMethodListener listener = new MessageMethodListener(object, method);
    String id = annotation.value();
    messEngine.addMessageListener(id, listener);
  }

  private void removeMessageMethodHandler(Object object, Method method) {
    checkMessEngine();
    MessageMethod annotation = method.getAnnotation(MessageMethod.class);
    MessageMethodListener listener = new MessageMethodListener(object, method);
    String id = annotation.value();
    messEngine.removeMessageListener(id, listener);
  }

  private void checkMessEngine() {
    if (messEngine == null) {
      throw new IllegalStateException("Autoconfigure failed no messEngine set.");
    }
  }

  private List<Method> getMethods(Class<?> objectClass, Class<? extends Annotation> annotationClass) {
    List<Method> methods = new ArrayList<>();
    getMethods(objectClass, annotationClass, methods);
    return methods;
  }

  private void getMethods(Class<?> objectClass, Class<? extends Annotation> annotationClass, List<Method> methods) {
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

  public MessEngine getMessEngine() {
    return messEngine;
  }

  public final void setMessEngine(MessEngine messEngine) {
    this.messEngine = messEngine;
  }

  private <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationType) {
    T annotation = clazz.getAnnotation(annotationType);
    if (annotation == null) {
      for (Class<?> interfaze : clazz.getInterfaces()) {
        annotation = findAnnotation(interfaze, annotationType);
        if (annotation != null) {
          return annotation;
        }
        annotation = findAnnotation(clazz.getSuperclass(), annotationType);
      }
    }
    return annotation;
  }

}
