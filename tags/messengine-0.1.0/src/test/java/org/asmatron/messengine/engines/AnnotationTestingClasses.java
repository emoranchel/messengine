package org.asmatron.messengine.engines;

import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.annotations.ActionMethod;
import org.asmatron.messengine.annotations.EventMethod;
import org.asmatron.messengine.annotations.RequestField;
import org.asmatron.messengine.annotations.RequestMethod;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.ValueEvent;

public interface AnnotationTestingClasses {
	interface TestTypes {
		String actionId = "actionId";
		String eventId = "eventId";
		String requestId = "requestId";
		String requestFieldId = "requestFieldId";
		ActionId<RequestAction<String, Integer>> request = ActionId.cm(requestId);
		ActionId<RequestAction<Void, String>> requestField = ActionId.cm(requestFieldId);
		ActionId<ValueAction<String>> action = ActionId.cm(actionId);
		EventId<ValueEvent<String>> event = EventId.ev(eventId);
	}

	class RequestMethodTester {
		public boolean requestHandled;

		@RequestMethod(TestTypes.requestId)
		public Integer handleRequest(String number) {
			requestHandled = true;
			return Integer.parseInt(number);
		}
	}

	class RequestFieldTester {
		@SuppressWarnings("unused")
		@RequestField(TestTypes.requestFieldId)
		private String value = "abc";
	}

	class ActionMethodTester {
		public String val = null;

		@ActionMethod(TestTypes.actionId)
		public void handleAction(ValueAction<String> valueAction) {
			val = valueAction.getValue();
		}
	}

	class EventMethodTester {
		public String val = null;

		@EventMethod(TestTypes.eventId)
		public void handleEvent(ValueEvent<String> valueEvent) {
			val = valueEvent.getValue();
		}
	}

	class DualEventTester {
		public String val1 = null;
		public String val2 = null;

		@EventMethod(TestTypes.eventId)
		public void handleEvent1(ValueEvent<String> valueEvent) {
			val1 = valueEvent.getValue();
		}

		@EventMethod(TestTypes.eventId)
		public void handleEvent2(ValueEvent<String> valueEvent) {
			val2 = valueEvent.getValue();
		}
	}

}
