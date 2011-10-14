package org.asmatron.messengine.i18n;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

public class DefaultMessages implements ConfigurableMessages {

	private final static Log log = LogFactory.getLog(DefaultMessages.class);

	public static final String DEFAULT_LANGUAGE_KEY = "default.language";
	public static final String BUNDLE_NOT_SET = "BUNDLE NOT SET";
	public static final String DEFULT_MESSAGE = "Message Not Available";

	public static final String EN_US = "en_US";
	public static final String ES_MX = "es_MX";

	private MessageSource bundle;

	private Locale locale = Locale.US;

	private Set<Internationalizable> internationalizableSet;

	public DefaultMessages(MessageSource bundle, Locale locale) {
		this(bundle);
		this.locale = locale;
	}

	public DefaultMessages(MessageSource bundle) {
		this();
		this.bundle = bundle;
	}

	public DefaultMessages() {
		internationalizableSet = Collections.synchronizedSet(new HashSet<Internationalizable>());
	}

	public String getMessage(String key) {
		Object[] args = null;
		return getMessage(key, args);
	}

	public String getMessage(String key, Object... parameter) {
		if (bundle != null) {
			if (parameter == null || parameter.length == 0) {
				parameter = null;
			}
			return bundle.getMessage(key, parameter, DEFULT_MESSAGE, locale);
		} else {
			return BUNDLE_NOT_SET;
		}
	}

	@PreDestroy
	public void clean() {
		synchronized (internationalizableSet) {
			internationalizableSet.clear();
		}
	}

	public void add(Internationalizable internationalizable) {
		synchronized (internationalizableSet) {
			if (internationalizableSet.add(internationalizable)) {
				internationalizable.internationalize(this);
			}
		}
	}

	public void remove(Internationalizable internationalizable) {
		synchronized (internationalizableSet) {
			internationalizableSet.remove(internationalizable);
		}
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					init();
				}
			});
		} catch (InterruptedException e) {
			log.error(e, e);
		} catch (InvocationTargetException e) {
			log.error(e, e);
		}
	}

	@Override
	public void init() {
		synchronized (internationalizableSet) {
			for (Internationalizable internationalizable : internationalizableSet) {
				internationalizable.internationalize(this);
			}
		}
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public SimpleDateFormat getFormatter(String formatKey) {
		String format = getMessage(formatKey);
		return new SimpleDateFormat(format, locale);
	}
}
