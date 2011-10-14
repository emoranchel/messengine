package org.asmatron.messengine.appControl;

import java.lang.reflect.Field;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.RequestAction;


class RequestFieldHandler implements ActionHandler<RequestAction<Object, Object>> {
	private final Object object;
	private final Field field;

	public RequestFieldHandler(Object object, Field field) {
		this.object = object;
		this.field = field;
	}

	@Override
	public void handle(RequestAction<Object, Object> requestAction) {
		Object responseValue = null;
		try {
			boolean accesible = true;
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			responseValue = field.get(object);
			if (!accesible) {
				field.setAccessible(false);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			requestAction.getCallback().onResponse(responseValue);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RequestFieldHandler other = (RequestFieldHandler) obj;
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		if (object == null) {
			if (other.object != null) {
				return false;
			}
		} else if (!object.equals(other.object)) {
			return false;
		}
		return true;
	}

}