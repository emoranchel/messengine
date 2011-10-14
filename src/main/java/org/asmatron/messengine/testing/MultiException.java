package org.asmatron.messengine.testing;

import java.util.ArrayList;
import java.util.List;

public class MultiException extends Exception {
	private static final long serialVersionUID = 1L;
	List<Throwable> exceptions = new ArrayList<Throwable>();
	String message = null;

	public MultiException() {
	}

	public void add(Throwable e) {
		if (e instanceof MultiException) {
			MultiException multis = (MultiException) e;
			for (Throwable e2 : multis.exceptions) {
				add(e2);
			}
		} else {
			exceptions.add(e);
			message = null;
		}
	}

	public void throww() throws MultiException {
		if (!exceptions.isEmpty()) {
			throw this;
		}
	}

	@Override
	public String getMessage() {
		if (message == null) {
			String message = "There were " + exceptions.size() + " errors";
			for (Throwable e : exceptions) {
				message += ", \n" + e.getMessage();
			}
			this.message = message;
		}
		return message;
	}

	@Override
	public Throwable getCause() {
		if (exceptions.size() == 1) {
			return exceptions.get(0);
		}
		return super.getCause();

	}
}
