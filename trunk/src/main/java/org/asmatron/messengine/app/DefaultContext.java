package org.asmatron.messengine.app;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultContext implements ApplicationContext {
	private static final Log log = LogFactory.getLog(BeanCreationMonitor.class);
	private List<EntityBean> entities = new ArrayList<EntityBean>();

	@Override
	public void close() {
		for (EntityBean byName : entities) {
			doPreDestroys(byName.object);
		}
	}

	private void doPreDestroys(Object ob) {
		Method[] methods = ob.getClass().getDeclaredMethods();
		for (Method method : methods) {
			checkInvokePredestroy(method, ob);
		}
	}

	private void checkInvokePredestroy(Method method, Object ob) {
		boolean annotationPresent = false;
		boolean noArguments = method.getParameterTypes().length == 0;
		boolean isVoid = method.getReturnType().equals(void.class);
		boolean isPublic = (method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC;
		boolean isNotStatic = (method.getModifiers() & Modifier.STATIC) != Modifier.STATIC;
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.getClass().getName().contains("PreDestroy") || annotation.toString().contains("PreDestroy")) {
				annotationPresent = true;
			}
		}
		if (annotationPresent && noArguments && isPublic && isNotStatic && isVoid) {
			try {
				method.invoke(ob);
			} catch (Exception e) {
				log.error(e, e);
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> clazz) {
		T exactMatch = null;
		T closeMatch = null;
		boolean hasError = false;
		for (EntityBean e : entities) {
			if (clazz.equals(e.object)) {
				if (exactMatch != null) {
					throw new MoreThanOneDefinitionException();
				} else {
					exactMatch = (T) e.object;
				}
			} else if (clazz.isAssignableFrom(e.object.getClass())) {
				if (closeMatch != null) {
					hasError = true;
				} else {
					closeMatch = (T) e.object;
				}
			}
		}
		if (exactMatch == null && hasError) {
			throw new MoreThanOneDefinitionException();
		}
		if (exactMatch == null && closeMatch == null) {
			throw new NoBeanDefinedException();
		}
		return exactMatch != null ? exactMatch : closeMatch;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String name, Class<T> clazz) {
		for (EntityBean e : entities) {
			if (name.equals(e.name) && clazz.isAssignableFrom(e.object.getClass())) {
				return (T) e.object;
			}
		}
		throw new NoBeanDefinedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getBean(String name) {
		return getBean(name, Object.class);
	}

	public void registerBean(String name, Object bean) {
		try {
			getBean(name, bean.getClass());
			throw new BeanAlreadyRegisteredException();
		} catch (NoBeanDefinedException ex) {
			entities.add(new EntityBean(name, bean));
		}
	}

	private class EntityBean {
		private final String name;
		private final Object object;

		public EntityBean(String name, Object object) {
			this.name = name;
			this.object = object;
		}
	}

}
