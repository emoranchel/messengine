package org.asmatron.messengine;

public interface EngineController {

  void start(Callback callback);

  void stop(Callback callback);

  void start();

  void stop();

  public void addEngineListener(EngineListener engineListener);

  public void removeEngineListener(EngineListener engineListener);
}
