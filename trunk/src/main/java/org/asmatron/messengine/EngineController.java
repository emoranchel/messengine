package org.asmatron.messengine;

public interface EngineController {
  void start();

  void stop();

  public void addEngineListener(EngineListener engineListener);

  public void removeEngineListener(EngineListener engineListener);
}
