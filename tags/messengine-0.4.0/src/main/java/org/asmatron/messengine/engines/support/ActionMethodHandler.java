package org.asmatron.messengine.engines.support;

import java.lang.reflect.Method;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.util.MethodInvoker;


public class ActionMethodHandler extends MethodInvoker implements ActionHandler<ActionObject> {
	public ActionMethodHandler(Object object, Method method) {
		super(object, method);
		if (method.getParameterTypes().length > 1) {
			throw new IllegalMethodException("Illegal Engine binding on: " + object.getClass().getName() + "." + method.getName());
		}
	}

	@Override
	public void handle(ActionObject arg) {
		if (arg instanceof ValueAction) {
			Object o = ((ValueAction<?>) arg).getValue();
			if (canInvoke(o)) {
				invoke(o);
				return;
			}
		}
		invoke(arg);
	}
}