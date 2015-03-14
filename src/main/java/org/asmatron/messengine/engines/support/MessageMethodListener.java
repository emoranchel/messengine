package org.asmatron.messengine.engines.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

public class MessageMethodListener<T> implements MessageListener<T> {

  public MessageMethodListener(Object object, Method method) {
    this.object = object;
    this.method = method;
    if (method.getParameterTypes().length > 1) {
      throw new IllegalArgumentException("Cannot bind a method with more than one argument!");
    }
  }

  @Override
  public void onMessage(Message<T> arg) {
    T o = arg.getBody();
    if (canInvoke(o)) {
      invoke(o);
      return;
    }
    invoke(arg);
  }

  private final Object object;
  private final Method method;

  public Object invoke(Object arg) {
    if (method.getParameterTypes().length == 0) {
      return invoke();
    }
    Object result = null;
    try {
      boolean accesible = true;
      if (!method.isAccessible()) {
        accesible = false;
        method.setAccessible(true);
      }
      result = method.invoke(object, arg);
      if (!accesible) {
        method.setAccessible(false);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return result;
  }

  public Object invoke() {
    Object result = null;
    try {
      boolean accesible = true;
      if (!method.isAccessible()) {
        accesible = false;
        method.setAccessible(true);
      }
      result = method.invoke(object);
      if (!accesible) {
        method.setAccessible(false);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    result = prime * result + ((object == null) ? 0 : object.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    MessageMethodListener other = (MessageMethodListener) obj;
    if (method == null) {
      if (other.method != null) {
        return false;
      }
    } else if (!method.equals(other.method)) {
      return false;
    }
    if (object == null) {
      if (other.object != null) {
        return false;
      }
    } else if (!object.equals(other.object)) {
      return false;
    }
    return true;
  }

  protected boolean canInvoke(Object o) {
    if (method.getParameterTypes().length == 0) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> paramType = method.getParameterTypes()[0];
    Class<? extends Object> objectType = o.getClass();
    return paramType.isAssignableFrom(objectType);
  }

  @Override
  public String toString() {
    return "INVOKE:" + method + " >> on " + object + " (" + object.getClass().getName() + ")";
  }

}
