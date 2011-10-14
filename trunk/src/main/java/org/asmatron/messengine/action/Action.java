package org.asmatron.messengine.action;

public class Action<T extends ActionObject> {
	private final ActionType<T> type;
	private final T param;

	public Action(ActionType<T> type, T param) {
		this.type = type;
		this.param = param;
	}

	public ActionType<T> getType() {
		return type;
	}

	public T getParam() {
		return param;
	}

}
