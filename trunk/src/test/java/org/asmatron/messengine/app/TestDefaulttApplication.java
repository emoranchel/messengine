package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.asmatron.messengine.app.AbstractApplication;
import org.asmatron.messengine.app.AppListener;
import org.asmatron.messengine.app.ApplicationAbortException;
import org.asmatron.messengine.app.Attributes;
import org.asmatron.messengine.app.DefaultApplication;
import org.asmatron.messengine.app.Module;
import org.asmatron.messengine.app.ResultProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class TestDefaulttApplication {
	@InjectMocks
	private AbstractApplication<Object> application;
	@Mock
	private Attributes attributes;
	private Module mainModule = mock(Module.class);

	@Before
	public void setup() {
		application = new DefaultApplication<Object>(mainModule);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCreateException() throws Exception {
		assertNotNull(new ApplicationAbortException());
	}

	@Test
	public void shouldCheckApplicationLifecycle() throws Exception {
		AppListener listener = mock(AppListener.class);

		application.addAppListener(listener);
		assertEquals(null, application.execute());
		inOrder(listener, mainModule);
		verify(listener).initialize();
		verify(mainModule).activate();
		verify(mainModule).execute(attributes);
		verify(mainModule).destroy();
		verify(listener).destroy();
	}

	@Test
	public void shouldDoAppWithProcessor() throws Exception {
		@SuppressWarnings("unchecked")
		ResultProcessor<Object> resultProcessor = mock(ResultProcessor.class);
		when(resultProcessor.result(attributes)).thenReturn("AA");

		application.setResultProcessor(resultProcessor);

		assertEquals("AA", application.execute());
	}

	@Test
	public void coveRAGE() throws Exception {
		new DefaultApplication<Void>();
	}
}
