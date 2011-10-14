package org.asmatron.messengine.appControl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionType;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.appControl.control.ControlEngine;


public class ControlEngineConfigurator {
	private ControlEngine controlEngine;

	public ControlEngineConfigurator() {
	}

	public ControlEngineConfigurator(ControlEngine controlEngine) {
		setControlEngine(controlEngine);
	}

	public int setupControlEngine(Object object) {
		List<Field> requestFields = AppAnnotationUtils.getFields(object.getClass(), RequestField.class);
		List<Method> requestMethods = AppAnnotationUtils.getMethods(object.getClass(), RequestMethod.class);
		List<Method> actionMethods = AppAnnotationUtils.getMethods(object.getClass(), ActionMethod.class);
		if ((requestFields.size() + requestMethods.size() + actionMethods.size()) == 0) {
			return 0;
		}
		for (Method method : requestMethods) {
			addRequestMethodHandler(object, method);
		}
		for (Method method : actionMethods) {
			addActionMethodHandler(object, method);
		}
		for (Field field : requestFields) {
			addRequestFieldHandler(object, field);
		}
		return requestFields.size() + requestMethods.size() + actionMethods.size();
	}

	public int resetControlEngine(Object object) {
		List<Field> requestFields = AppAnnotationUtils.getFields(object.getClass(), RequestField.class);
		List<Method> requestMethods = AppAnnotationUtils.getMethods(object.getClass(), RequestMethod.class);
		List<Method> actionMethods = AppAnnotationUtils.getMethods(object.getClass(), ActionMethod.class);
		if ((requestFields.size() + requestMethods.size() + actionMethods.size()) == 0) {
			return 0;
		}
		for (Method method : requestMethods) {
			removeRequestMethodHandler(object, method);
		}
		for (Method method : actionMethods) {
			removeActionMethodHandler(object, method);
		}
		for (Field field : requestFields) {
			removeRequestFieldHandler(object, field);
		}
		return requestFields.size() + requestMethods.size() + actionMethods.size();
	}

	private void addRequestFieldHandler(Object object, Field field) {
		checkControlEngine();
		RequestField annotation = field.getAnnotation(RequestField.class);
		ActionType<RequestAction<Object, Object>> actionType = ActionType.cm(annotation.value());
		RequestFieldHandler actionHandler = new RequestFieldHandler(object, field);
		controlEngine.addActionHandler(actionType, actionHandler);
	}

	private void addRequestMethodHandler(Object object, Method method) {
		checkControlEngine();
		RequestMethod annotation = method.getAnnotation(RequestMethod.class);
		ActionType<RequestAction<Object, Object>> actionType = ActionType.cm(annotation.value());
		RequestMethodHandler actionHandler = new RequestMethodHandler(object, method);
		controlEngine.addActionHandler(actionType, actionHandler);
	}

	private void addActionMethodHandler(Object object, Method method) {
		checkControlEngine();
		ActionMethod annotation = method.getAnnotation(ActionMethod.class);
		ActionType<ActionObject> actionType = ActionType.cm(annotation.value());
		ActionMethodHandler actionHandler = new ActionMethodHandler(object, method);
		controlEngine.addActionHandler(actionType, actionHandler);
	}

	private void removeRequestFieldHandler(Object object, Field field) {
		checkControlEngine();
		RequestField annotation = field.getAnnotation(RequestField.class);
		ActionType<RequestAction<Object, Object>> actionType = ActionType.cm(annotation.value());
		controlEngine.removeActionHandler(actionType);
	}

	private void removeActionMethodHandler(Object object, Method method) {
		checkControlEngine();
		ActionMethod annotation = method.getAnnotation(ActionMethod.class);
		ActionType<ActionObject> actionType = ActionType.cm(annotation.value());
		controlEngine.removeActionHandler(actionType);
	}

	private void removeRequestMethodHandler(Object object, Method method) {
		checkControlEngine();
		RequestMethod annotation = method.getAnnotation(RequestMethod.class);
		ActionType<RequestAction<Object, Object>> actionType = ActionType.cm(annotation.value());
		controlEngine.removeActionHandler(actionType);
	}

	private void checkControlEngine() {
		if (controlEngine == null) {
			throw new IllegalStateException("Autoconfigure failed no ControlEngine set.");
		}
	}

	public ControlEngine getControlEngine() {
		return controlEngine;
	}

	public void setControlEngine(ControlEngine controlEngine) {
		this.controlEngine = controlEngine;
	}

}
