package org.asmatron.messengine.i18n;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.asmatron.messengine.i18n.DefaultMessages;
import org.asmatron.messengine.i18n.Internationalizable;
import org.asmatron.messengine.i18n.Messages;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;


public class TestDefaultMessages {
	private static final String MESSAGE_CODE_1 = "mess.1";
	private static final String MESSAGE_CODE_2 = "mess.2";
	private static final String MESSAGE_1 = "message 1";
	private static final String MESSAGE_2 = "message 2";
	private static final String MESSAGE_1_FR = "message french 1";
	private static final String MESSAGE_2_FR = "message french 2";
	private static final String MESSAGE_1_CH = "message china 1";
	private static final String MESSAGE_2_CH = "message china 2";
	private static final String MESSAGE_1_CA = "message canada 1";
	private static final String MESSAGE_2_CA = "message canada 2";
	private static final String DEFAULT = "DEFAULT MESSAGE";
	@Mock
	private MessageSource bundle;
	private DefaultMessages messages;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		messages = new DefaultMessages();
		assertEquals("BUNDLE NOT SET", messages.getMessage(MESSAGE_CODE_1));
		messages = new DefaultMessages(bundle);
		Object[] nullArr = null;
		when(bundle.getMessage(anyString(), eq(nullArr), anyString(), isA(Locale.class))).thenReturn(DEFAULT);
		when(bundle.getMessage(eq(MESSAGE_CODE_1), eq(nullArr), anyString(), isA(Locale.class))).thenReturn(MESSAGE_1);
		when(bundle.getMessage(eq(MESSAGE_CODE_2), eq(nullArr), anyString(), isA(Locale.class))).thenReturn(MESSAGE_2);

		when(bundle.getMessage(eq(MESSAGE_CODE_1), eq(nullArr), anyString(), eq(Locale.FRENCH))).thenReturn(MESSAGE_1_FR);
		when(bundle.getMessage(eq(MESSAGE_CODE_2), eq(nullArr), anyString(), eq(Locale.FRENCH))).thenReturn(MESSAGE_2_FR);

		when(bundle.getMessage(eq(MESSAGE_CODE_1), eq(nullArr), anyString(), eq(Locale.CANADA))).thenReturn(MESSAGE_1_CA);
		when(bundle.getMessage(eq(MESSAGE_CODE_2), eq(nullArr), anyString(), eq(Locale.CANADA))).thenReturn(MESSAGE_2_CA);

		when(bundle.getMessage(eq(MESSAGE_CODE_1), eq(nullArr), anyString(), eq(Locale.CHINA))).thenReturn(MESSAGE_1_CH);
		when(bundle.getMessage(eq(MESSAGE_CODE_2), eq(nullArr), anyString(), eq(Locale.CHINA))).thenReturn(MESSAGE_2_CH);
	}

	@Test
	public void shouldTestTheMessages() throws Exception {
		assertEquals(MESSAGE_1, messages.getMessage(MESSAGE_CODE_1));
		assertEquals(MESSAGE_2, messages.getMessage(MESSAGE_CODE_2));
		assertEquals(DEFAULT, messages.getMessage("ASDASDSA"));

		messages.setLocale(Locale.CANADA);
		assertEquals(Locale.CANADA, messages.getLocale());
		assertEquals(MESSAGE_1_CA, messages.getMessage(MESSAGE_CODE_1));
		assertEquals(MESSAGE_2_CA, messages.getMessage(MESSAGE_CODE_2));
		assertEquals(DEFAULT, messages.getMessage("ASDASDSA"));

		messages.setLocale(Locale.FRENCH);
		assertEquals(MESSAGE_1_FR, messages.getMessage(MESSAGE_CODE_1));
		assertEquals(MESSAGE_2_FR, messages.getMessage(MESSAGE_CODE_2));
		assertEquals(DEFAULT, messages.getMessage("ASDASDSA"));

		messages.setLocale(Locale.CHINA);
		assertEquals(MESSAGE_1_CH, messages.getMessage(MESSAGE_CODE_1));
		assertEquals(MESSAGE_2_CH, messages.getMessage(MESSAGE_CODE_2));
		assertEquals(DEFAULT, messages.getMessage("ASDASDSA"));
	}

	@Test
	public void shouldTestInternationalizable() throws Exception {
		TestInternationalizable internationalizable = new TestInternationalizable();
		messages.add(internationalizable);
		assertEquals(MESSAGE_1, internationalizable.message);
		messages.setLocale(Locale.CHINA);
		assertEquals(MESSAGE_1_CH, internationalizable.message);
		messages.remove(internationalizable);
		messages.setLocale(Locale.FRENCH);
		assertEquals(MESSAGE_1_CH, internationalizable.message);
		messages.add(internationalizable);
		assertEquals(MESSAGE_1_FR, internationalizable.message);
		messages.clean();
		messages.setLocale(Locale.CANADA);
		assertEquals(MESSAGE_1_FR, internationalizable.message);

	}

	class TestInternationalizable implements Internationalizable {
		private String message;

		@Override
		public void setMessages(Messages messages) {
		}

		@Override
		public void removeMessages(Messages messages) {
		}

		@Override
		public void internationalize(Messages messages) {
			message = messages.getMessage(MESSAGE_CODE_1);
		}
	};
}
