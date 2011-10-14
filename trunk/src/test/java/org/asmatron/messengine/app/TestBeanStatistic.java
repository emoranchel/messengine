package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.app.BeanStatistic;
import org.junit.Before;
import org.junit.Test;


public class TestBeanStatistic {
	private static final Log log = LogFactory.getLog(TestBeanStatistic.class);

	private BeanStatistic statistic;

	@Before
	public void setup() {
		statistic = BeanStatistic.create().instantiate("a", Object.class);
	}

	@Test
	public void shouldGetSimpleStatisticData() throws Exception {
		assertTrue(statistic.getInstantiationTime() >= 0);
		assertEquals(BeanStatistic.toId("a", Object.class), statistic.id());
		assertEquals("java.lang.Object::a", statistic.id());
	}

	@Test
	public void shouldCompareByTotalTimes() throws Exception {
		BeanStatistic other = null;

		other = mock(BeanStatistic.class);
		when(other.getTotalTime()).thenReturn(statistic.getTotalTime() + 1);
		assertEquals(-1, statistic.compareTo(other));

		other = mock(BeanStatistic.class);
		when(other.getTotalTime()).thenReturn(statistic.getTotalTime() - 1);
		assertEquals(1, statistic.compareTo(other));

		assertEquals(0, statistic.compareTo(statistic));

		assertEquals(1, statistic.compareTo(null));
	}

	@Test
	public void shouldDemonstrateTheUssageAndLyfecycleOfStatistics() throws Exception {
		// Before this point the stat has only been instantiated.
		// Calls to getAutowireTime returns 0.
		log.info(statistic.toString());
		statistic.beginAutowire();
		statistic.endAutowire();
		// now we have autowire data and we know this thing has its autowired properties set. Time spent from here until the
		// next call is called Limbo Time since nothing happens in the bean or its direct dependencies, the VM or spring
		// might be slacking or maybe its constructing other objects
		log.info(statistic.toString());
		// This is the time for postConstructs!
		statistic.beginInitialization();
		statistic.endInitialization();
		// Now we have a complete trace
		log.info(statistic.toString());

	}

	@Test
	public void coveRAGE() throws Exception {
		statistic.toString();
	}
}
