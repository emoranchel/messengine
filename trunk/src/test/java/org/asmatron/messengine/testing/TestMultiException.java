package org.asmatron.messengine.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.asmatron.messengine.testing.MultiException;
import org.junit.Test;


public class TestMultiException {
	@Test
	public void testname() throws Exception {
		MultiException mex = new MultiException();
		assertNotNull(mex.getMessage());
		mex.throww();
	}

	@Test(expected = MultiException.class)
	public void anotherTest() throws Exception {
		MultiException mex = new MultiException();
		RuntimeException ex = new RuntimeException();
		mex.add(ex);
		assertEquals(ex, mex.getCause());
		assertNotNull(mex.getMessage());
		mex.throww();
	}

	@Test(expected = MultiException.class)
	public void anotherTest2() throws Exception {
		MultiException mex = new MultiException();
		RuntimeException ex1 = new RuntimeException();
		RuntimeException ex2 = new RuntimeException();
		mex.add(ex1);
		mex.add(ex2);
		assertNull(mex.getCause());
		assertNotNull(mex.getMessage());
		mex.throww();
	}

	@Test(expected = MultiException.class)
	public void shouldAddMultiExceptionContents() throws Exception {
		MultiException mex1 = new MultiException();
		MultiException mex2 = new MultiException();
		RuntimeException ex1 = new RuntimeException();
		RuntimeException ex2 = new RuntimeException();
		mex1.add(ex1);
		mex2.add(ex2);
		mex1.add(mex2);
		assertNull(mex1.getCause());
		assertNotNull(mex1.getMessage());
		mex1.throww();
	}
}
