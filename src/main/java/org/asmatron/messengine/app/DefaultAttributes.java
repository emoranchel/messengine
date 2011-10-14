package org.asmatron.messengine.app;

import java.util.HashMap;
import java.util.Map;

public class DefaultAttributes implements Attributes {
	private Map<String, Object> attributes = new HashMap<String, Object>();

	@Override
	public Object getAttribute(Attribute key) {
		return attributes.get(key.name());
	}

	@Override
	public void setAttribute(Attribute key, Object value) {
		attributes.put(key.name(), value);
	}

}
