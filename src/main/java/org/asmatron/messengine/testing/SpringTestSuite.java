package org.asmatron.messengine.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.app.ApplicationContext;
import org.asmatron.messengine.app.BeanStatistics;
import org.asmatron.messengine.app.Spring;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public abstract class SpringTestSuite {
	private static Log log = LogFactory.getLog(SpringTestSuite.class);
	protected ApplicationContext appContext;
	private List<BeanToTest> beans = new ArrayList<BeanToTest>();
	protected BeanStatistics statistics;

	@Before
	public void setup() throws Exception {
		try {
			Spring spring = Spring.load(configurations());
			List<Object> additionalBeans = additionalBeans();
			if (additionalBeans != null) {
				for (Object o : additionalBeans) {
					spring.addBean(o);
				}
			}
			appContext = spring.debug();
			statistics = appContext.getBean(BeanStatistics.class);
		} catch (Exception e) {
			log.error(e, e);
			throw e;
		}
	}

	protected List<Object> additionalBeans() {
		return null;
	}

	@After
	public void teardown() throws Exception {
		try {
			appContext.close();
		} catch (Exception e) {
			log.error(e, e);
			throw e;
		}
	}

	public abstract String[] configurations();

	public abstract void initBeans() throws Exception;

	public void testBean(Class<?> clazz, String... fields) {
		beans.add(new BeanToTest(clazz, fields));
	}

	@Test
	public void shouldTestSpring() throws Exception {
		initBeans();
		MultiException ex = new MultiException();
		for (BeanToTest bean : beans) {
			try {
				AppContextTestUtils.checkBean(appContext, bean.clazz, bean.properties);
			} catch (Throwable e) {
				ex.add(e);
			}
		}
		try {
			verify();
		} catch (Throwable e) {
			ex.add(e);
		}

		try {
			ex.throww();
		} catch (Exception e) {
			log.error(e, e);
			for (String s : configurations()) {
				printConfig(s);
			}
			throw e;
		}
	}

	protected void verify() throws Exception {
	}

	private void printConfig(String config) throws IOException {
		InputStream inS = getClass().getResourceAsStream(config);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inS));
		String res = "-- \n\n == " + config + " ==\n";
		String s = null;
		while ((s = reader.readLine()) != null) {
			res += s + "\n";
		}
		reader.close();
		log.info(res);
	}

	private class BeanToTest {
		private final Class<?> clazz;
		private final String[] properties;

		public BeanToTest(Class<?> clazz, String[] properties) {
			this.clazz = clazz;
			this.properties = properties;
		}
	}

}
