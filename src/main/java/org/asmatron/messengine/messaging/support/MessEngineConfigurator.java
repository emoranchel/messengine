package org.asmatron.messengine.messaging.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.asmatron.messengine.messaging.MessEngine;
import org.asmatron.messengine.messaging.MessageMethod;


public class MessEngineConfigurator {
	private MessEngine messEngine;

	public MessEngineConfigurator() {
	}

	public MessEngineConfigurator(MessEngine messEngine) {
		setMessEngine(messEngine);
	}

	public int setupMessEngine(Object object) {
		List<Method> messageMethods = getMethods(object.getClass(), MessageMethod.class);
		if (messageMethods.size() == 0) {
			return 0;
		}
		for (Method method : messageMethods) {
			addMessageMethodHandler(object, method);
		}
		return messageMethods.size();
	}

	public int resetMessEngine(Object object) {
		List<Method> messageMethods = getMethods(object.getClass(), MessageMethod.class);
		if (messageMethods.size() == 0) {
			return 0;
		}
		for (Method method : messageMethods) {
			removeMessageMethodHandler(object, method);
		}
		return messageMethods.size();
	}

	private void addMessageMethodHandler(Object object, Method method) {
		checkMessEngine();
		MessageMethod annotation = method.getAnnotation(MessageMethod.class);
		MessageMethodListener listener = new MessageMethodListener(object, method);
		String id = annotation.value();
		messEngine.addMessageListener(id, listener);
	}

	private void removeMessageMethodHandler(Object object, Method method) {
		checkMessEngine();
		MessageMethod annotation = method.getAnnotation(MessageMethod.class);
		MessageMethodListener listener = new MessageMethodListener(object, method);
		String id = annotation.value();
		messEngine.removeMessageListener(id, listener);
	}

	private void checkMessEngine() {
		if (messEngine == null) {
			throw new IllegalStateException("Autoconfigure failed no messEngine set.");
		}
	}

	private List<Method> getMethods(Class<?> objectClass, Class<? extends Annotation> annotationClass) {
		List<Method> methods = new ArrayList<Method>();
		getMethods(objectClass, annotationClass, methods);
		return methods;
	}

	private void getMethods(Class<?> objectClass, Class<? extends Annotation> annotationClass, List<Method> methods) {
		if (objectClass != null) {
			Method[] declaredMethods = objectClass.getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (method.isAnnotationPresent(annotationClass)) {
					methods.add(method);
				}
			}
			getMethods(objectClass.getSuperclass(), annotationClass, methods);
		}
	}

	public MessEngine getMessEngine() {
		return messEngine;
	}

	public void setMessEngine(MessEngine messEngine) {
		this.messEngine = messEngine;
	}

}
