package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.asmatron.messengine.app.BeanStatistic;
import org.asmatron.messengine.app.BeanStatistics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class TestBeanStatistics {
	private static final String STAT_2_NAME = "!b";
	private static final String STAT_1_NAME = "!a";
	private static final String STAT_1_ID = BeanStatistic.toId(STAT_1_NAME, Object.class);
	private static final String STAT_2_ID = BeanStatistic.toId(STAT_2_NAME, Object.class);
	private BeanStatistics statistics = new BeanStatistics();
	@Mock
	private BeanStatistic stat1;
	@Mock
	private BeanStatistic stat2;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(stat1.id()).thenReturn(STAT_1_ID);
		when(stat2.id()).thenReturn(STAT_2_ID);
	}

	@Test
	public void shouldAddStatistic() throws Exception {
		statistics.add(stat1);
		assertEquals(stat1, statistics.get(STAT_1_NAME, Object.class));
		assertNotNull(statistics.get("ss", Object.class));
	}

	@Test
	public void shouldMeasureTotalTime() throws Exception {
		statistics.end();
		assertTrue(statistics.limboTime() >= 0);
		assertTrue(statistics.totalTime() >= 0);
	}

	@Test
	public void shouldGetTheBean() throws Exception {
		shouldAddStatistic();
		List<BeanStatistic> stats = statistics.getStats();
		assertNotNull(stats);
		assertFalse(stats.isEmpty());
		assertEquals(1, stats.size());
		assertEquals(stat1, stats.get(0));
	}

	@Test
	public void coveRage() throws Exception {
		shouldAddStatistic();
		statistics.getDescription();
	}
}
