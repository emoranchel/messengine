package org.asmatron.messengine.app;

import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

public class DefaultSpringFactory implements SpringFactory {

	@Override
	public GenericApplicationContext newGenericApplicationContext(DefaultListableBeanFactory beanMonitor) {
		if (beanMonitor == null) {
			return new GenericApplicationContext();
		}
		return new GenericApplicationContext(beanMonitor);
	}

	@Override
	public ParameterNameDiscoverer newLocalVariableTableParameterNameDiscoverer() {
		return new LocalVariableTableParameterNameDiscoverer();
	}

	@Override
	public AutowireCandidateResolver newQualifierAnnotationAutowireCandidateResolver() {
		return new QualifierAnnotationAutowireCandidateResolver();
	}

	@Override
	public XmlBeanDefinitionReader newXmlBeanDefinitionReader(BeanDefinitionRegistry appContext) {
		return new XmlBeanDefinitionReader(appContext);
	}

}
