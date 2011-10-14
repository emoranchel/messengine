package org.asmatron.messengine.appControl.control;

public class ActionFailedToExecute extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ActionFailedToExecute(Throwable cause, StackTraceElement[] stack) {
		super(cause);
		setStackTrace(stack);
	}
}
