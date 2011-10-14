package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.asmatron.messengine.app.DefaultContext;
import org.junit.Test;


public class TestDefaultContext {
	private DefaultContext context = new DefaultContext();

	@Test
	public void shouldAddBeansToThisContextAndDestroyThems() throws Exception {
		TestBean bean = new TestBean();
		context.registerBean("name", bean);
		assertFalse(bean.preDestroyRan);
		assertFalse(bean.postConstructRan);

		assertEquals(bean, context.getBean(TestBean.class));
		assertEquals(bean, context.getBean("name"));
		assertEquals(bean, context.getBean("name", TestBean.class));

		context.close();
		assertTrue(bean.preDestroyRan);
	}

	@SuppressWarnings("unused")
	private class TestBean {
		boolean postConstructRan = false;
		boolean preDestroyRan = false;

		@PostConstruct
		public void post() {
			postConstructRan = true;
		}

		@PreDestroy
		public void pre() {
			preDestroyRan = true;
		}
	}
}
