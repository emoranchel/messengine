package org.asmatron.messengine.engines.components;

public class ActionFailedToExecute extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ActionFailedToExecute(Throwable cause, StackTraceElement[] stack) {
		super(cause);
		setStackTrace(stack);
	}
}
