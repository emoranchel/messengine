package org.asmatron.messengine.engines;

import static org.junit.Assert.assertEquals;

import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.annotations.ActionMethod;
import org.asmatron.messengine.annotations.EventMethod;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.testing.TestEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestUnboxing {
	private final TestEngine engine = new TestEngine();
	private final EventListener ev = new EventListener();
	private final ActionListener ac = new ActionListener();

	@Before
	public void setup() {
		engine.setup(ev);
		engine.setup(ac);
	}

	@After
	public void teardown() {
		engine.reset(ev);
		engine.reset(ac);
	}

	@Test
	public void shouldUnboxAction() throws Exception {
		engine.send(new ActionId<ValueAction<String>>("actionUnboxing"), new ValueAction<String>("aaa"));
		assertEquals("aaa", ac.action);
	}

	@Test
	public void shouldUnboxEvent() throws Exception {
		engine.fireEvent(new EventId<ValueEvent<String>>("eventUnboxing"), new ValueEvent<String>("bbb"));
		assertEquals("bbb", ev.event);
	}

}

class EventListener {
	public String event;

	@EventMethod("eventUnboxing")
	public void onEvent(String event) {
		this.event = event;
	}
}

class ActionListener {
	public String action;

	@ActionMethod("actionUnboxing")
	public void onAction(String action) {
		this.action = action;
	}
}