package org.asmatron.messengine.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;

import org.asmatron.messengine.app.BeanStatistic;
import org.asmatron.messengine.app.BeanStatistics;
import org.asmatron.messengine.app.BeanStatisticsWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class TestBeanStatisticWriter {
	@Mock
	private BeanStatistics statistics;
	@Mock
	private BeanStatistic stat1;
	@Mock
	private BeanStatistic stat2;
	private BeanStatisticsWriter writer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		writer = new BeanStatisticsWriter(statistics);
		ArrayList<BeanStatistic> stats = new ArrayList<BeanStatistic>();
		stats.add(stat1);
		stats.add(stat2);

		when(statistics.getStats()).thenReturn(stats);
	}

	@Test
	public void shouldDeleteFileYEahIKnowItsInsecureAndBlahBlah() throws Exception {
		File createTempFile = File.createTempFile("testAllBSW", "SDFYEIKUIBB");
		createTempFile.deleteOnExit();
		assertTrue(createTempFile.exists());
		writer.deleteFile(createTempFile.getAbsolutePath());
		assertFalse(createTempFile.exists());
	}

	@Test
	public void shouldSaveToCSV() throws Exception {
		File createTempFile = File.createTempFile("testAllBSW", "SSTCSV");
		String filename = createTempFile.getAbsolutePath();
		writer.deleteFile(filename).saveToCSVFile(filename);
	}
}
