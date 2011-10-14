package org.asmatron.messengine.i18n;

import java.text.SimpleDateFormat;

public interface Messages {
	public String getMessage(String key);

	public SimpleDateFormat getFormatter(String formatKey);
	
	public String getMessage(String key, Object... parameter);

	public void add(Internationalizable internationalizable);

	public void remove(Internationalizable internationalizable);
}
