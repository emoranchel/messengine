package org.asmatron.messengine.engines.support;

import java.lang.reflect.Method;

import org.asmatron.messengine.EngineListener;
import org.asmatron.messengine.util.MethodInvoker;

public class StartEngineMethodHandler extends MethodInvoker implements
        EngineListener {

  public StartEngineMethodHandler(Object object, Method method) {
    super(object, method);
  }

  @Override
  public void onEngineStart() {
    invoke();
  }

  @Override
  public void onEngineStop() {
  }

}
