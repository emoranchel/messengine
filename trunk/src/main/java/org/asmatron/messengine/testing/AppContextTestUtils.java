package org.asmatron.messengine.testing;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.app.ApplicationContext;
import org.junit.Ignore;


@Ignore
public class AppContextTestUtils {
	private static Log log = LogFactory.getLog(AppContextTestUtils.class);

	public static void checkBean(ApplicationContext appContext, Class<?> clazz, String... properties) throws Exception {
		MultiException ex = new MultiException();
		log.debug("Checking... " + clazz.getName());
		Object bean = appContext.getBean(clazz);
		try {
			assertNotNull(bean);
			if (properties != null) {
				for (String string : properties) {
					try {
						checkField(bean, string);
					} catch (Throwable e) {
						ex.add(e);
					}
				}
			}
		} catch (Throwable e) {
			ex.add(e);
		}
		ex.throww();
	}

	public static void checkField(Object object, String name) throws Exception {
		log.debug("Checking... " + object.getClass().getName() + ":" + name);
		Field declaredField = object.getClass().getDeclaredField(name);
		declaredField.setAccessible(true);
		assertNotNull("Field " + name + " on " + object.getClass().getName() + " is null", declaredField.get(object));
	}
}
