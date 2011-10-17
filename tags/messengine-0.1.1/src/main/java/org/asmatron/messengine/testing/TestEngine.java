package org.asmatron.messengine.testing;

import org.asmatron.messengine.engines.DefaultEngine;
import org.asmatron.messengine.engines.support.ControlEngineConfigurator;
import org.asmatron.messengine.engines.support.MessagingConfigurator;
import org.asmatron.messengine.engines.support.ViewEngineConfigurator;
import org.asmatron.messengine.testing.support.TestActionDelegate;
import org.asmatron.messengine.testing.support.TestEventDelegate;
import org.asmatron.messengine.testing.support.TestMessagingDelegate;
import org.asmatron.messengine.testing.support.TestModelDelegate;

public class TestEngine extends DefaultEngine {
	private ControlEngineConfigurator controlConfigurator;
	private ViewEngineConfigurator viewConfigurator;
	private MessagingConfigurator messagingConfigurator;

	public void setup(Object object) {
		getControlConfigurator().setupControlEngine(object);
		getViewConfigurator().setupViewEngine(object);
		getMessagingConfigurator().setupMessEngine(object);
	}

	public void reset(Object object) {
		getViewConfigurator().resetViewEngine(object);
		getControlConfigurator().resetControlEngine(object);
		getMessagingConfigurator().resetMessEngine(object);
	}

	private ControlEngineConfigurator getControlConfigurator() {
		if (controlConfigurator == null) {
			controlConfigurator = new ControlEngineConfigurator(this);
		}
		return controlConfigurator;
	}

	private ViewEngineConfigurator getViewConfigurator() {
		if (viewConfigurator == null) {
			viewConfigurator = new ViewEngineConfigurator(this);
		}
		return viewConfigurator;
	}

	public MessagingConfigurator getMessagingConfigurator() {
		if (messagingConfigurator == null) {
			messagingConfigurator = new MessagingConfigurator(this);
		}
		return messagingConfigurator;
	}

	public TestEngine() {
		super(new TestActionDelegate(), new TestEventDelegate(), new TestMessagingDelegate(), new TestModelDelegate());
		start();
	}
}
