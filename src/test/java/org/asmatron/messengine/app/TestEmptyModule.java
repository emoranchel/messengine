package org.asmatron.messengine.app;

import org.asmatron.messengine.app.EmptyModule;
import org.junit.Test;


public class TestEmptyModule {
	@Test
	public void emptyModuleDoesNothing() throws Exception {
		EmptyModule module = new EmptyModule();
		module.activate();
		module.execute(null);
		module.destroy();
	}
}
