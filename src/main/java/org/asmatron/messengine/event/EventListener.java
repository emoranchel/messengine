package org.asmatron.messengine.event;

public abstract class EventListener<T extends EventObject> implements Listener<T> {

	private EventExecutionMode mode;
	private boolean eager;

	public EventListener() {
		this(EventExecutionMode.NORMAL, false);
	}

	public EventListener(EventExecutionMode mode) {
		this(mode, false);
	}

	public EventListener(boolean eager) {
		this(EventExecutionMode.NORMAL, eager);
	}

	public EventListener(EventExecutionMode mode, boolean eager) {
		this.mode = mode;
		this.eager = eager;
	}

	@Override
	public EventExecutionMode getMode() {
		return mode;
	}

	@Override
	public boolean isEager() {
		return eager;
	}

}
