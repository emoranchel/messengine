package org.asmatron.messengine.app;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Window;
import java.awt.event.WindowListener;

import org.asmatron.messengine.app.ApplicationUtils;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class TestApplicationUtils {

	@Test(timeout = 1000)
	public void shouldWaitForFrameToClose() throws Exception {
		Window window = mock(Window.class);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				WindowListener listener = (WindowListener) invocation.getArguments()[0];
				listener.windowClosing(null);
				return null;
			}
		}).when(window).addWindowListener(any(WindowListener.class));
		ApplicationUtils.showFrameAndWaitForClose(window);
		verify(window).setVisible(true);
	}

	@Test(timeout = 1000)
	public void coveRAGE() throws Exception {
		new ApplicationUtils();
		Window window = mock(Window.class);
		doThrow(new RuntimeException()).when(window).setVisible(true);
		ApplicationUtils.showFrameAndWaitForClose(window);
	}
}
