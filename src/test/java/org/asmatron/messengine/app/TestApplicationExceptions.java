package org.asmatron.messengine.app;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.asmatron.messengine.app.AppListener;
import org.asmatron.messengine.app.Application;
import org.asmatron.messengine.app.ApplicationAbortException;
import org.asmatron.messengine.app.Attributes;
import org.asmatron.messengine.app.DefaultApplication;
import org.asmatron.messengine.app.Module;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class TestApplicationExceptions {
	@Mock
	private Attributes attributes;
	@Mock
	private Module mainModule;
	@Mock
	private AppListener listener;

	@InjectMocks
	private Application<Object> app;

	@Before
	public void setup() {
		app = new DefaultApplication<Object>(mainModule);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldStopInInitializer() throws Exception {
		app.addAppListener(listener);

		doThrow(new ApplicationAbortException()).when(listener).initialize();

		app.execute();
		verify(listener).initialize();
		verify(listener, never()).destroy();
	}

	@Test
	public void shouldStopInPhase() throws Exception {
		app.addAppListener(listener);

		doThrow(new ApplicationAbortException()).when(mainModule).execute(attributes);

		app.execute();
		verify(mainModule).execute(attributes);
		verify(listener, never()).destroy();

	}

	@Test
	public void shouldStopInDestroy() throws Exception {
		AppListener listener1 = mock(AppListener.class);
		app.addAppListener(listener);
		app.addAppListener(listener1);

		doThrow(new ApplicationAbortException()).when(listener).destroy();

		app.execute();
		verify(listener).destroy();
		verify(listener1, never()).destroy();

	}

	@Test
	public void shouldKeepGoingOnLesserErrorOnInitialize() throws Exception {
		AppListener listener1 = mock(AppListener.class);
		app.addAppListener(listener);
		app.addAppListener(listener1);

		doThrow(new IndexOutOfBoundsException()).when(listener).initialize();
		doThrow(new IndexOutOfBoundsException()).when(mainModule).execute(attributes);
		doThrow(new IndexOutOfBoundsException()).when(listener).destroy();

		app.execute();
		verify(listener).initialize();
		verify(mainModule).execute(attributes);
		verify(listener).destroy();
		verify(listener1).destroy();

	}

}
