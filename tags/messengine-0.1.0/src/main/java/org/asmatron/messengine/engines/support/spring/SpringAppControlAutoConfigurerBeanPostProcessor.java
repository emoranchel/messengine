package org.asmatron.messengine.engines.support.spring;

import org.asmatron.messengine.ControlEngine;
import org.asmatron.messengine.MessEngine;
import org.asmatron.messengine.ViewEngine;
import org.asmatron.messengine.engines.Engine;
import org.asmatron.messengine.engines.support.EngineConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class SpringAppControlAutoConfigurerBeanPostProcessor implements DestructionAwareBeanPostProcessor {
	private final EngineConfigurator engineConfigurator;

	public SpringAppControlAutoConfigurerBeanPostProcessor(Engine engine) {
		engineConfigurator = new EngineConfigurator(engine);
	}

	public SpringAppControlAutoConfigurerBeanPostProcessor(ViewEngine viewEngine, ControlEngine controlEngine,
			MessEngine messEngine) {
		engineConfigurator = new EngineConfigurator(viewEngine, controlEngine, messEngine);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		engineConfigurator.setup(bean);
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		engineConfigurator.reset(bean);
	}

}
