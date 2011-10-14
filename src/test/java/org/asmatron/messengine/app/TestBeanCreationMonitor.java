package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.asmatron.messengine.app.BeanCreationMonitor;
import org.asmatron.messengine.app.BeanStatistic;
import org.asmatron.messengine.app.BeanStatistics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.RootBeanDefinition;


public class TestBeanCreationMonitor {
	private BeanStatistics statistics;
	private BeanCreationMonitor monitor;

	@Before
	public void setup() {
		statistics = mock(BeanStatistics.class);
		MockitoAnnotations.initMocks(this);
		monitor = new BeanCreationMonitor(statistics);
	}

	@Test
	public void shouldTestBasicStuff() throws Exception {
		assertEquals(statistics, monitor.getStatistics());
	}

	@Test
	public void checkPopulateBeanTime() throws Exception {
		monitor = spy(monitor);
		BeanWrapper bw = mock(BeanWrapper.class);
		RootBeanDefinition mbd = new RootBeanDefinition();
		String beanName = "a";
		String bean = "aaa";
		BeanStatistic stat = mock(BeanStatistic.class);

		when(monitor.doSuperPopulateBean(beanName, mbd, bw)).thenReturn(false);
		when(bw.getWrappedInstance()).thenReturn(bean);
		when(statistics.get(beanName, bean.getClass())).thenReturn(stat);

		monitor.populateBean(beanName, mbd, bw);

		verify(stat).beginAutowire();
		verify(stat).endAutowire();
	}

	@Test
	public void checkCreateBeanInstance() throws Exception {
		monitor = spy(monitor);
		BeanWrapper bw = mock(BeanWrapper.class);
		RootBeanDefinition mbd = new RootBeanDefinition();
		mbd.setBeanClass(String.class);
		String beanName = "a";
		String bean = "aaa";

		when(monitor.doSuperCreateBeanInstance(beanName, mbd, null)).thenReturn(bw);
		when(bw.getWrappedInstance()).thenReturn(bean);

		BeanWrapper createBeanInstance = monitor.createBeanInstance(beanName, mbd, null);
		assertEquals(bw, createBeanInstance);

		ArgumentCaptor<BeanStatistic> statisticCaptor = ArgumentCaptor.forClass(BeanStatistic.class);
		verify(statistics).add(statisticCaptor.capture());
		BeanStatistic stat = statisticCaptor.getValue();
		assertNotNull(stat);
		assertEquals(stat.getClassName(), bean.getClass().getName());
		assertEquals(stat.getName(), beanName);
	}

	@Test(expected = BeanCreationException.class)
	public void ifThingsGoesAwryOnCreateBeanInstanceItShouldExplodeWithStyle() throws Exception {
		String beanName = "a";
		RootBeanDefinition mbd = new RootBeanDefinition();
		monitor.createBeanInstance(beanName, mbd, null);
	}

	@Test
	public void shouldPostProcessBeforeInitializationOfABean() throws Exception {
		String beanName = "a";
		String bean = "aaa";
		BeanStatistic stat = mock(BeanStatistic.class);
		when(statistics.get(beanName, bean.getClass())).thenReturn(stat);
		monitor.postProcessBeforeInitialization(bean, beanName);
		verify(stat).beginInitialization();
	}

	@Test
	public void shouldPostProcessAfterInitializationOfABean() throws Exception {
		String beanName = "a";
		String bean = "aaa";
		BeanStatistic stat = mock(BeanStatistic.class);
		when(statistics.get(beanName, bean.getClass())).thenReturn(stat);
		monitor.postProcessAfterInitialization(bean, beanName);
		verify(stat).endInitialization();
	}

	@Test
	public void coveRAGE() throws Exception {
		new BeanCreationMonitor();
		RootBeanDefinition mbd = new RootBeanDefinition();
		mbd.setBeanClass(Object.class);
		monitor.doSuperCreateBeanInstance("a", mbd, null);
		monitor.doSuperPopulateBean("a", mbd, new BeanWrapperImpl());
	}
}
