package org.asmatron.messengine.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventMethod {
	String value();

	boolean eager() default false;

	EventExecutionMode mode() default EventExecutionMode.NORMAL;
}
