package org.asmatron.messengine.action;

public class Action<T extends ActionObject> {
	private final ActionId<T> type;
	private final T param;

	public Action(ActionId<T> type, T param) {
		this.type = type;
		this.param = param;
	}

	public ActionId<T> getType() {
		return type;
	}

	public T getParam() {
		return param;
	}

}
