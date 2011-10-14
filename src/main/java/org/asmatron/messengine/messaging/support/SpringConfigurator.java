package org.asmatron.messengine.messaging.support;

import javax.annotation.PostConstruct;

import org.asmatron.messengine.messaging.MessEngine;
import org.asmatron.messengine.messaging.MessageListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;


@Component("messEngineSpringConfigurator")
public class SpringConfigurator implements DestructionAwareBeanPostProcessor {
	@Autowired
	private MessEngine messEngine;
	private MessEngineConfigurator messEngineConfigurator;

	@PostConstruct
	public void setup() {
		messEngineConfigurator = new MessEngineConfigurator(messEngine);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		MessageSelector selector = AnnotationUtils.findAnnotation(bean.getClass(), MessageSelector.class);
		if (selector != null && bean instanceof MessageListener) {
			MessageListener listener = (MessageListener) bean;
			messEngine.addMessageListener(selector.value(), listener);
		}
		messEngineConfigurator.setupMessEngine(bean);
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		messEngineConfigurator.resetMessEngine(bean);
	}
}
