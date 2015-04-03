package org.asmatron.messengine.engines.support;

import java.lang.reflect.Method;

import org.asmatron.messengine.EngineListener;
import org.asmatron.messengine.util.MethodInvoker;

public class EngineListenerMethodHandler extends MethodInvoker implements
        EngineListener {

  private final Mode mode;

  public enum Mode {

    STARTING, STARTED, STOPING, STOPED
  }

  public EngineListenerMethodHandler(Object object, Method method, Mode mode) {
    super(object, method);
    this.mode = mode;
  }

  @Override
  public void onEngineStarting() {
    if (mode == Mode.STARTING) {
      invoke();
    }
  }

  @Override
  public void onEngineStarted() {
    if (mode == Mode.STARTED) {
      invoke();
    }
  }

  @Override
  public void onEngineStoping() {
    if (mode == Mode.STOPING) {
      invoke();
    }
  }

  @Override
  public void onEngineStoped() {
    if (mode == Mode.STOPED) {
      invoke();
    }
  }

}
