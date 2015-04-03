package org.asmatron.messengine.engines.support;

import org.asmatron.messengine.ControlEngine;
import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.ViewEngine;
import org.asmatron.messengine.engines.Engine;

public class EngineConfigurator {

  private final ViewEngineConfigurator viewEngineConfigurator;
  private final ControlEngineConfigurator controlEngineConfigurator;
  private final MessagingConfigurator messagingConfigurator;
  private final EngineListenerConfigurator engineListenerConfigurator;

  public EngineConfigurator(Engine engine) {
    this(engine, engine, engine);
  }

  public EngineConfigurator(ViewEngine viewEngine, ControlEngine controlEngine, MessEngine messEngine) {
    this.viewEngineConfigurator = new ViewEngineConfigurator(viewEngine);
    this.controlEngineConfigurator = new ControlEngineConfigurator(controlEngine);
    this.messagingConfigurator = new MessagingConfigurator(messEngine);
    this.engineListenerConfigurator = new EngineListenerConfigurator(controlEngine);
  }

  public void setup(Object... beans) {
    for (Object bean : beans) {
      setup(bean);
    }
  }

  public void reset(Object... beans) {
    for (Object bean : beans) {
      reset(bean);
    }
  }

  public void setup(Object bean) {
    viewEngineConfigurator.setupViewEngine(bean);
    controlEngineConfigurator.setupControlEngine(bean);
    messagingConfigurator.setupMessEngine(bean);
    engineListenerConfigurator.setupListeners(bean);
  }

  public void reset(Object bean) {
    viewEngineConfigurator.resetViewEngine(bean);
    controlEngineConfigurator.resetControlEngine(bean);
    messagingConfigurator.resetMessEngine(bean);
    engineListenerConfigurator.resetListeners(bean);

  }
}
