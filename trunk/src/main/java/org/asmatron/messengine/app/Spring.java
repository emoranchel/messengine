package org.asmatron.messengine.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

public class Spring {
	private List<String> configs = new ArrayList<String>();
	private List<SpringBean> beans = new ArrayList<SpringBean>();
	private SpringFactory springFactory;

	private Spring() {
		springFactory = new DefaultSpringFactory();
	}

	public Spring addConfig(String... configs) {
		if (configs != null) {
			for (String string : configs) {
				this.configs.add(string);
			}
		}
		return this;
	}

	public Spring addBean(Object object) {
		this.beans.add(new SpringBean(null, object));
		return this;
	}

	public Spring addBean(Object object, String name) {
		this.beans.add(new SpringBean(name, object));
		return this;
	}

	public ApplicationContext debug() {
		BeanCreationMonitor beanMonitor = new BeanCreationMonitor();

		GenericApplicationContext appContext = springFactory.newGenericApplicationContext(beanMonitor);

		beanMonitor.setSerializationId(appContext.getId());
		beanMonitor.setParameterNameDiscoverer(springFactory.newLocalVariableTableParameterNameDiscoverer());
		beanMonitor.setAutowireCandidateResolver(springFactory.newQualifierAnnotationAutowireCandidateResolver());

		appContext.getBeanFactory().addBeanPostProcessor(beanMonitor);
		loadBeans(appContext);
		appContext.getBeanFactory().registerSingleton("BeanStatistics", beanMonitor.getStatistics());
		loadConfigs(appContext);

		beanMonitor.getStatistics().end();
		return new SpringGenericApplicationContext(appContext);
	}

	public ApplicationContext load() {
		GenericApplicationContext appContext = springFactory.newGenericApplicationContext(null);
		loadBeans(appContext);
		loadConfigs(appContext);
		return new SpringGenericApplicationContext(appContext);
	}

	private void loadConfigs(GenericApplicationContext appContext) {
		XmlBeanDefinitionReader reader = springFactory.newXmlBeanDefinitionReader(appContext);
		String[] array = (String[]) configs.toArray(new String[configs.size()]);
		reader.loadBeanDefinitions(array);
		appContext.refresh();
	}

	private void loadBeans(GenericApplicationContext appContext) {
		if (!beans.isEmpty()) {
			for (SpringBean bean : beans) {
				if (!appContext.containsBean(bean.name)) {
					appContext.getBeanFactory().registerSingleton(bean.name, bean.object);
				}
			}
		}
	}

	public static Spring newSpring() {
		return new Spring();
	}

	public static Spring load(String... configs) {
		return newSpring().addConfig(configs);
	}

	private class SpringBean {
		private final Object object;
		private final String name;

		public SpringBean(String name, Object object) {
			name = name == null ? object.getClass().getSimpleName() : name;
			this.name = name;
			this.object = object;
		}
	}

	private class SpringGenericApplicationContext implements ApplicationContext {
		private final GenericApplicationContext appContext;

		public SpringGenericApplicationContext(GenericApplicationContext appContext) {
			this.appContext = appContext;
		}

		@Override
		public <T> T getBean(Class<T> clasz) {
			return appContext.getBean(clasz);
		}

		@Override
		public <T> T getBean(String name, Class<T> clasz) {
			return appContext.getBean(name, clasz);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getBean(String name) {
			return (T) appContext.getBean(name);
		}

		@Override
		public void close() {
			appContext.close();
		}
	}
}
