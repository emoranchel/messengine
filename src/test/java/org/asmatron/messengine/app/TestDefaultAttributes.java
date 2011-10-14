package org.asmatron.messengine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.asmatron.messengine.app.Attribute;
import org.asmatron.messengine.app.Attributes;
import org.asmatron.messengine.app.DefaultAttributes;
import org.junit.Test;


public class TestDefaultAttributes {
	@Test
	public void shouldTestDefaultAttributes() throws Exception {
		Attributes attrs = new DefaultAttributes();
		assertNull(attrs.getAttribute(TestAttributes.A));
		attrs.setAttribute(TestAttributes.A, "A");
		assertEquals("A", attrs.getAttribute(TestAttributes.A));
	}

	enum TestAttributes implements Attribute {
		A, B;
	}
}
