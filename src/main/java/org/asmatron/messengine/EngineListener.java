package org.asmatron.messengine;

public interface EngineListener {

  void onEngineStarting();

  void onEngineStarted();

  void onEngineStoping();

  void onEngineStoped();
}
