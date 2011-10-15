package org.asmatron.messengine.event;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.asmatron.messengine.engines.DefaultEventDelegate;
import org.asmatron.messengine.event.EventExecutionMode;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.EventType;
import org.asmatron.messengine.event.Listener;
import org.junit.Test;


public class TestEvents {
	public static final EventType<CustomEvent> aType = new EventType<CustomEvent>("a");

	@Test
	public void shouldTestEventEngine() throws Exception {
		final AtomicBoolean invoked = new AtomicBoolean(false);

		DefaultEventDelegate engine = new DefaultEventDelegate();

		engine.addListener(aType, new Listener<CustomEvent>() {
			@Override
			public void handleEvent(CustomEvent eventArgs) {
				invoked.set(true);
			}

			@Override
			public EventExecutionMode getMode() {
				return EventExecutionMode.NORMAL;
			}

			@Override
			public boolean isEager() {
				return false;
			}
		});

		engine.fireEvent(aType, new CustomEvent());
		assertTrue(invoked.get());
	}
}

class CustomEvent extends EventObject {
}
