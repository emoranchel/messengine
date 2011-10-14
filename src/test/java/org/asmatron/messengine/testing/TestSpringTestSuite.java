package org.asmatron.messengine.testing;

import static org.junit.Assert.fail;

import org.asmatron.messengine.testing.MultiException;
import org.asmatron.messengine.testing.SpringTestSuite;
import org.junit.Test;


public class TestSpringTestSuite {
	@Test
	public void shouldCreateANewSpringTestSuiteAndTestWhateverThatCanBeTested() throws Exception {
		// which is not much though
		SpringTestSuite suite = new SpringTestSuite() {
			@Override
			public void initBeans() throws Exception {
				testBean(Object.class, "a", "b");
			}

			@Override
			public String[] configurations() {
				return new String[] { "/ignoredImposibleSpringContext/ignored.xml" };
			}

			@Override
			protected void verify() throws Exception {
				throw new RuntimeException();
			}
		};
		suite.setup();
		try {
			suite.shouldTestSpring();
			fail();
		} catch (MultiException e) {
		}
		suite.teardown();
	}
}
