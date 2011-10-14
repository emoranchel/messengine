package org.asmatron.messengine.messaging.support;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target(TYPE)
@Retention(RUNTIME)
@Component
public @interface MessageSelector {
	String value();
}
