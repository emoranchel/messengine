package org.asmatron.messengine.appControl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class SpringAppControlAutoConfigurerBeanPostProcessor implements DestructionAwareBeanPostProcessor {
	private final ViewEngineConfigurator viewEngineConfigurator;
	private final ControlEngineConfigurator controlEngineConfigurator;

	public SpringAppControlAutoConfigurerBeanPostProcessor(ViewEngineConfigurator configurer,
			ControlEngineConfigurator controlEngineConfigurator) {
		this.viewEngineConfigurator = configurer;
		this.controlEngineConfigurator = controlEngineConfigurator;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		viewEngineConfigurator.setupViewEngine(bean);
		controlEngineConfigurator.setupControlEngine(bean);
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		viewEngineConfigurator.resetViewEngine(bean);
		controlEngineConfigurator.resetControlEngine(bean);
	}

}
