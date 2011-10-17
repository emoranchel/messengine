package org.asmatron.messengine.action;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.DuplicateActionHandlerException;
import org.asmatron.messengine.engines.DefaultActionDelegate;
import org.asmatron.messengine.testing.support.TestActionDelegate;
import org.junit.Test;


public class TestDefaultCommandEngine {
	public static final ActionId<DemoCommandObject> cType = new ActionId<DemoCommandObject>("c");

	@Test
	public void testeamesta() throws Exception {
		DefaultActionDelegate engine = new TestActionDelegate();
		final AtomicBoolean handled = new AtomicBoolean(false);

		engine.addActionHandler(cType, new ActionHandler<DemoCommandObject>() {
			@Override
			public void handle(DemoCommandObject arg) {
				handled.set(true);
			}
		});
		engine.send(cType.create(new DemoCommandObject()));
		assertTrue(handled.get());
	}

	@Test(expected = DuplicateActionHandlerException.class)
	public void shouldNotAddTwoHandlers() throws Exception {
		DefaultActionDelegate engine = new TestActionDelegate();

		engine.addActionHandler(cType, new ActionHandler<DemoCommandObject>() {
			@Override
			public void handle(DemoCommandObject arg) {
			}
		});
		engine.addActionHandler(cType, new ActionHandler<DemoCommandObject>() {
			@Override
			public void handle(DemoCommandObject arg) {
			}
		});
	}
}

class DemoCommandObject extends ActionObject {
}