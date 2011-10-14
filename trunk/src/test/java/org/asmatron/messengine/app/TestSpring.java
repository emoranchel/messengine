package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.asmatron.messengine.app.ApplicationContext;
import org.asmatron.messengine.app.BeanCreationMonitor;
import org.asmatron.messengine.app.BeanStatistics;
import org.asmatron.messengine.app.Spring;
import org.asmatron.messengine.app.SpringFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ParameterNameDiscoverer;


//@Ignore("It breaks with emma on hudson, but works fine whatsoever")
public class TestSpring {
	private static final String CONFIG = "config";
	@InjectMocks
	private Spring spring = Spring.load("config");
	@Mock
	private SpringFactory springFac;
	private Object bean = new Object();

	private GenericApplicationContext genericAppContext;
	private ParameterNameDiscoverer paramNameDisc;
	private AutowireCandidateResolver autowireCand;
	private XmlBeanDefinitionReader xmlReader;
	private DefaultListableBeanFactory beanFactory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		beanFactory = mock(DefaultListableBeanFactory.class);

		genericAppContext = spy(new GenericApplicationContext(beanFactory));
		paramNameDisc = mock(ParameterNameDiscoverer.class);
		autowireCand = mock(AutowireCandidateResolver.class);
		xmlReader = mock(XmlBeanDefinitionReader.class);

		when(springFac.newGenericApplicationContext(any(DefaultListableBeanFactory.class))).thenReturn(genericAppContext);
		when(springFac.newLocalVariableTableParameterNameDiscoverer()).thenReturn(paramNameDisc);
		when(springFac.newQualifierAnnotationAutowireCandidateResolver()).thenReturn(autowireCand);
		when(springFac.newXmlBeanDefinitionReader(any(BeanDefinitionRegistry.class))).thenReturn(xmlReader);

		when(genericAppContext.getBean(Object.class)).thenReturn(bean);
		when(genericAppContext.getBean("bean")).thenReturn(bean);
		when(genericAppContext.getBean("bean", Object.class)).thenReturn(bean);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(genericAppContext).refresh();

	}

	@Test
	public void shouldConstructSpring() throws Exception {
		spring.addBean(bean);
		spring.addConfig(CONFIG);
		ApplicationContext appContext = spring.load();
		assertEquals(bean, appContext.getBean("bean"));
		assertEquals(bean, appContext.getBean(Object.class));
		assertEquals(bean, appContext.getBean("bean", Object.class));
		appContext.close();
		verify(genericAppContext).close();
	}

	@Test
	public void shouldAddBeansToSpringYEAH() throws Exception {
		spring.addBean(bean);
		String singletonObject = new String();
		spring.addBean(singletonObject, "bbb");
		spring.load();
		verify(beanFactory).registerSingleton("bbb", singletonObject);
		verify(beanFactory).registerSingleton(bean.getClass().getSimpleName(), bean);
	}

	@Test
	public void shouldSimpleLoad() throws Exception {
		spring.load();
		verify(xmlReader).loadBeanDefinitions(new String[] { "config" });
	}

	@Test
	public void shouldDebug() throws Exception {
		spring.debug();
		verify(beanFactory).addBeanPostProcessor(any(BeanCreationMonitor.class));
		verify(beanFactory).registerSingleton(eq("BeanStatistics"), any(BeanStatistics.class));

	}
}
