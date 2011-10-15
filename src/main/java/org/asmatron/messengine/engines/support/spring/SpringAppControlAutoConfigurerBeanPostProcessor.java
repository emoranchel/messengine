package org.asmatron.messengine.engines.support.spring;

import org.asmatron.messengine.ControlEngine;
import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.ViewEngine;
import org.asmatron.messengine.engines.Engine;
import org.asmatron.messengine.engines.support.ControlEngineConfigurator;
import org.asmatron.messengine.engines.support.MessagingConfigurator;
import org.asmatron.messengine.engines.support.ViewEngineConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class SpringAppControlAutoConfigurerBeanPostProcessor implements DestructionAwareBeanPostProcessor {
	private final ViewEngineConfigurator viewEngineConfigurator;
	private final ControlEngineConfigurator controlEngineConfigurator;
	private final MessagingConfigurator messagingConfigurator;

	public SpringAppControlAutoConfigurerBeanPostProcessor(Engine engine) {
		this(engine, engine, engine);
	}

	public SpringAppControlAutoConfigurerBeanPostProcessor(ViewEngine viewEngine, ControlEngine controlEngine,
			MessEngine messEngine) {
		this.viewEngineConfigurator = new ViewEngineConfigurator(viewEngine);
		this.controlEngineConfigurator = new ControlEngineConfigurator(controlEngine);
		this.messagingConfigurator = new MessagingConfigurator(messEngine);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		viewEngineConfigurator.setupViewEngine(bean);
		controlEngineConfigurator.setupControlEngine(bean);
		messagingConfigurator.setupMessEngine(bean);
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		viewEngineConfigurator.resetViewEngine(bean);
		controlEngineConfigurator.resetControlEngine(bean);
		messagingConfigurator.resetMessEngine(bean);
	}

}
