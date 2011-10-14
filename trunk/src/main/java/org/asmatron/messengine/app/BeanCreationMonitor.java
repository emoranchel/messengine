package org.asmatron.messengine.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.app.BeanStatistic.BeanStatisticFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;


public class BeanCreationMonitor extends DefaultListableBeanFactory implements BeanFactory, BeanPostProcessor {
	private static final Log log = LogFactory.getLog(BeanCreationMonitor.class);
	private final BeanStatistics statistics;

	public BeanCreationMonitor() {
		this(new BeanStatistics());
	}

	public BeanCreationMonitor(BeanStatistics statistics) {
		this.statistics = statistics;
	}

	// Useful bookmark, here is the core of spring instantiation thus you will find salvation
	// If you see this class it will be full of challenges, but fear not
	// as you finish all the challenges there will be cake awaiting for you and your
	// friends will be invited, too bad, no one will be actually attending,
	// you have no friends.
	// @Override
	// protected Object doCreateBean(String beanName, RootBeanDefinition mbd, Object[] args) {
	// return super.doCreateBean(beanName, mbd, args);
	// }

	@Override
	protected void populateBean(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw) {
		statistics.get(beanName, bw.getWrappedInstance().getClass()).beginAutowire();
		doSuperPopulateBean(beanName, mbd, bw);
		statistics.get(beanName, bw.getWrappedInstance().getClass()).endAutowire();
	}

	@Override
	public BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args) {
		try {
			BeanStatisticFactory stat = BeanStatistic.create();
			BeanWrapper bean = doSuperCreateBeanInstance(beanName, mbd, args);
			statistics.add(stat.instantiate(beanName, bean.getWrappedInstance().getClass()));
			return bean;
		} catch (BeansException e) {
			log.error(e, e);
			throw e;
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		statistics.get(beanName, bean.getClass()).beginInitialization();
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		statistics.get(beanName, bean.getClass()).endInitialization();
		return bean;
	}

	public BeanStatistics getStatistics() {
		return statistics;
	}

	// For great testing
	public BeanWrapper doSuperCreateBeanInstance(String beanName, RootBeanDefinition mbd, Object[] args) {
		return super.createBeanInstance(beanName, mbd, args);
	}

	// For great testing
	// THE CAKE IS A LIE!
	public boolean doSuperPopulateBean(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw) {
		super.populateBean(beanName, mbd, bw);
		return true;
	}
}
