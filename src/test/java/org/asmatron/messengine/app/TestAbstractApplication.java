package org.asmatron.messengine.app;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.asmatron.messengine.app.AbstractApplication;
import org.asmatron.messengine.app.ApplicationAbortException;
import org.asmatron.messengine.app.Attributes;
import org.asmatron.messengine.app.Module;
import org.junit.Test;


public class TestAbstractApplication {
	private Attributes attributes = mock(Attributes.class);
	private Module module = mock(Module.class);
	private AbstractApplication<Void> app = new AbstractApplication<Void>(module) {

		@Override
		protected Attributes getAttributes() {
			return attributes;
		}
	};

	@Test
	public void shouldSetAndRunAnotherModule() throws Exception {
		Module module2 = mock(Module.class);
		app.setMainModule(module2);
		app.execute();
		verify(module2).activate();
		verify(module2).execute(attributes);
		verify(module2).destroy();
	}

	@Test
	public void shouldKeepGoingWithExceptionsInModule() throws Exception {
		doThrow(new RuntimeException()).when(module).activate();
		doThrow(new RuntimeException()).when(module).execute(attributes);
		doThrow(new RuntimeException()).when(module).destroy();
		app.execute();
		verify(module).activate();
		verify(module).execute(attributes);
		verify(module).destroy();
	}

	@Test
	public void shouldHaltIfAbortedOnInit() throws Exception {
		doThrow(new ApplicationAbortException()).when(module).activate();
		app.execute();
		verify(module).activate();
		verify(module, never()).execute(attributes);
		verify(module, never()).destroy();
	}

	@Test
	public void shouldHaltIfAbortedOnDestroy() throws Exception {
		doThrow(new ApplicationAbortException()).when(module).destroy();
		app.execute();
		verify(module).activate();
		verify(module).execute(attributes);
		verify(module).destroy();
	}

	@Test
	public void shouldHaltIfAbortedOnMain() throws Exception {
		doThrow(new ApplicationAbortException()).when(module).execute(attributes);
		app.execute();
		verify(module).activate();
		verify(module).execute(attributes);
		verify(module, never()).destroy();
	}
}
