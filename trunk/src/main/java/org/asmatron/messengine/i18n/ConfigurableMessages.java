package org.asmatron.messengine.i18n;

import java.util.Locale;

public interface ConfigurableMessages extends Messages {
	void setLocale(Locale locale);

	void init();

	void clean();

}
