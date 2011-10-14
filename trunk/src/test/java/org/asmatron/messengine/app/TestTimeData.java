package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;

import org.asmatron.messengine.app.TimeData;
import org.junit.Before;
import org.junit.Test;


public class TestTimeData {
	private TimeData data = new TimeData(3);

	@Before
	public void setup() {
		data.add(4000);
		data.add(1000);
		data.add(1000);
	}

	@Test
	public void shouldGetAboveAverage() throws Exception {
		assertEquals(1, data.getAboveAverage());
	}

	@Test
	public void shouldGetBelowAverage() throws Exception {
		assertEquals(2, data.getBelowAverage());
	}

	@Test
	public void shouldGetAverage() throws Exception {
		assertEquals(2000, data.getAverage());
	}

	@Test
	public void shouldGetTotal() throws Exception {
		assertEquals(6000, data.getTotal());
	}

	@Test
	public void shouldGetMaximum() throws Exception {
		assertEquals(4000, data.getMaximum());
	}

	@Test
	public void coveRAGE() throws Exception {
		TimeData data = new TimeData(0);
		data.getAboveAverage();
	}
}
