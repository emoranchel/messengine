package org.asmatron.messengine.testing;

import static org.junit.Assert.fail;

import org.asmatron.messengine.app.ApplicationContext;
import org.asmatron.messengine.testing.AppContextTestUtils;
import org.junit.Test;
import org.mockito.Mockito;


public class TestAppContextTestUtils {

	@Test
	public void testTheTest() throws Exception {
		TestBean testBean = new TestBean();
		try {
			AppContextTestUtils.checkField(testBean, "a");
			fail();
		} catch (Throwable t) {
		}
		AppContextTestUtils.checkField(testBean, "b");
		AppContextTestUtils.checkField(testBean, "c");

		ApplicationContext appContext = Mockito.mock(ApplicationContext.class);
		Mockito.when(appContext.getBean(TestBean.class)).thenReturn(testBean);

		try {
			AppContextTestUtils.checkBean(appContext, TestBean.class, "a", "b", "c", "d");
			fail();
		} catch (Throwable t) {
		}

	}

	@SuppressWarnings("unused")
	private class TestBean {
		String a = null;
		String b = "";
		String c = "";
	}
}
