package org.asmatron.messengine.app;

import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ParameterNameDiscoverer;

public interface SpringFactory {

	GenericApplicationContext newGenericApplicationContext(DefaultListableBeanFactory beanMonitor);

	ParameterNameDiscoverer newLocalVariableTableParameterNameDiscoverer();

	AutowireCandidateResolver newQualifierAnnotationAutowireCandidateResolver();

	XmlBeanDefinitionReader newXmlBeanDefinitionReader(BeanDefinitionRegistry appContext);

}
