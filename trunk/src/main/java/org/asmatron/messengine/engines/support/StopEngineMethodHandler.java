package org.asmatron.messengine.engines.support;

import java.lang.reflect.Method;

import org.asmatron.messengine.EngineListener;
import org.asmatron.messengine.util.MethodInvoker;

public class StopEngineMethodHandler extends MethodInvoker implements
    EngineListener {

  public StopEngineMethodHandler(Object object, Method method) {
    super(object, method);
  }

  @Override
  public void onEngineStart() {
  }

  @Override
  public void onEngineStop() {
    invoke();
  }

}
