package org.asmatron.messengine.action;


public class ActionProcessor<T extends ActionObject> {
	private ActionHandler<T> handler;
	private ActionType<T> type;

	public ActionProcessor(ActionType<T> type) {
		this.type = type;
	}

	public void handle(ActionHandler<T> handler) {
		if (this.handler != null) {
			throw new DuplicateActionHandlerException(this.handler, handler, type);
		}
		this.handler = handler;
	}

	public void clean() {
		handler = null;
	}

	public void fire(T param) {
		if (handler == null) {
			throw new NoHandlerException("No handler found for action: " + type.getId());
		} else {
			handler.handle(param);
		}
	}
}
