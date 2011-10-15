package org.asmatron.messengine.action;

public class DuplicateActionHandlerException extends RuntimeException {
	private final ActionHandler<?> newHandler;
	private final ActionHandler<?> oldHandler;
	private final ActionId<?> action;

	public DuplicateActionHandlerException(ActionHandler<?> newHandler, ActionHandler<?> oldHandler, ActionId<?> action) {
		super(getMessage(newHandler, oldHandler, action));
		this.newHandler = newHandler;
		this.oldHandler = oldHandler;
		this.action = action;
	}

	private static String getMessage(ActionHandler<?> newHandler, ActionHandler<?> oldHandler, ActionId<?> action) {
		StringBuffer str = new StringBuffer();
		str.append("Duplicate handler for action:");
		str.append(action);
		str.append(" Tried: ");
		str.append(newHandler.getClass().getName());
		str.append(newHandler.toString());
		str.append(" Was: ");
		str.append(oldHandler.getClass().getName());
		str.append(oldHandler.toString());
		return str.toString();
	}

	private static final long serialVersionUID = 1L;

	public ActionHandler<?> getNewHandler() {
		return newHandler;
	}

	public ActionHandler<?> getOldHandler() {
		return oldHandler;
	}

	public ActionId<?> getAction() {
		return action;
	}

}
