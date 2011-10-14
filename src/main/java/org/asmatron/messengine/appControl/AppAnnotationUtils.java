package org.asmatron.messengine.appControl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppAnnotationUtils {

	public static List<Method> getMethods(Class<?> objectClass, Class<? extends Annotation> annotationClass) {
		List<Method> methods = new ArrayList<Method>();
		getMethods(objectClass, annotationClass, methods);
		return methods;
	}

	public static void getMethods(Class<?> objectClass, Class<? extends Annotation> annotationClass, List<Method> methods) {
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

	public static List<Field> getFields(Class<?> objectClass, Class<? extends Annotation> annotationClass) {
		List<Field> fields = new ArrayList<Field>();
		getFields(objectClass, annotationClass, fields);
		return fields;
	}

	public static void getFields(Class<?> objectClass, Class<? extends Annotation> annotationClass, List<Field> fields) {
		if (objectClass != null) {
			Field[] declaredFields = objectClass.getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(annotationClass)) {
					fields.add(field);
				}
			}
			getFields(objectClass.getSuperclass(), annotationClass, fields);
		}
	}

}
