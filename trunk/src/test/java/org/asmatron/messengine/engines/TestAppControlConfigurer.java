package org.asmatron.messengine.engines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionType;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.engines.AnnotationTestingClasses.ActionMethodTester;
import org.asmatron.messengine.engines.AnnotationTestingClasses.DualEventTester;
import org.asmatron.messengine.engines.AnnotationTestingClasses.EventMethodTester;
import org.asmatron.messengine.engines.AnnotationTestingClasses.RequestFieldTester;
import org.asmatron.messengine.engines.AnnotationTestingClasses.RequestMethodTester;
import org.asmatron.messengine.engines.AnnotationTestingClasses.TestTypes;
import org.asmatron.messengine.engines.support.ControlEngineConfigurator;
import org.asmatron.messengine.engines.support.ViewEngineConfigurator;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.testing.TestEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class TestAppControlConfigurer {
	@Spy
	private TestEngine engine = new TestEngine();
	@SuppressWarnings("unchecked")
	@Captor
	private ArgumentCaptor<ActionType> actionTypeCaptor;
	@SuppressWarnings("unchecked")
	@Captor
	private ArgumentCaptor<ActionHandler> actionHandlerCaptor;
	private ViewEngineConfigurator viewConfigurator;
	private ControlEngineConfigurator controlConfigurator;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		viewConfigurator = new ViewEngineConfigurator(engine);
		controlConfigurator = new ControlEngineConfigurator(engine);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldBindAnnotationsToActionEngine() throws Exception {
		RequestMethodTester annotationTester = new RequestMethodTester();
		controlConfigurator.setupControlEngine(annotationTester);

		verify(engine).addActionHandler(actionTypeCaptor.capture(), actionHandlerCaptor.capture());
		ActionType type = actionTypeCaptor.getValue();
		ActionHandler handler = actionHandlerCaptor.getValue();
		assertEquals(TestTypes.requestId, type.getId());
		assertEquals(TestTypes.request, type);

		assertNotNull(handler);
		assertFalse(annotationTester.requestHandled);
		handler.handle(new RequestAction<String, Integer>("5", EmptyCallback.get(Integer.class)));
		assertTrue(annotationTester.requestHandled);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldBindActionMethodsCorrectly() throws Exception {
		ActionMethodTester annotationTester = new ActionMethodTester();
		controlConfigurator.setupControlEngine(annotationTester);

		verify(engine).addActionHandler(actionTypeCaptor.capture(), actionHandlerCaptor.capture());
		ActionType type = actionTypeCaptor.getValue();
		ActionHandler handler = actionHandlerCaptor.getValue();

		assertEquals(TestTypes.actionId, type.getId());
		assertEquals(TestTypes.action, type);

		assertNotNull(handler);
		assertNull(annotationTester.val);
		handler.handle(new ValueAction("a"));
		assertEquals("a", annotationTester.val);

	}

	@Test
	public void shouldTestRequestMethod() throws Exception {
		RequestMethodTester annotationTester = new RequestMethodTester();
		controlConfigurator.setupControlEngine(annotationTester);
		final AtomicBoolean requestResponded = new AtomicBoolean(false);
		ResponseCallback<Integer> callback = new ResponseCallback<Integer>() {
			@Override
			public void onResponse(Integer t) {
				assertEquals(Integer.valueOf(12), t);
				requestResponded.set(true);
			}
		};
		engine.request(TestTypes.request, "12", callback);
		assertTrue(requestResponded.get());
	}

	@Test
	public void shouldRequestField() throws Exception {
		RequestFieldTester annotationTester = new RequestFieldTester();
		controlConfigurator.setupControlEngine(annotationTester);
		final AtomicReference<String> val = new AtomicReference<String>(null);
		engine.request(TestTypes.requestField, null, new ResponseCallback<String>() {
			@Override
			public void onResponse(String t) {
				val.set(t);
			}
		});
		assertEquals("abc", val.get());
	}

	@Test
	public void shouldManageAction() throws Exception {
		ActionMethodTester annotationTester = new ActionMethodTester();
		controlConfigurator.setupControlEngine(annotationTester);
		engine.send(TestTypes.action, new ValueAction<String>("aer"));
		assertEquals("aer", annotationTester.val);
	}

	@Test
	public void shouldManageEvent() throws Exception {
		EventMethodTester annotationTester = new EventMethodTester();
		viewConfigurator.setupViewEngine(annotationTester);
		engine.fireEvent(TestTypes.event, new ValueEvent<String>("air"));
		assertEquals("air", annotationTester.val);
	}

	@Test
	public void shouldTestDualEvents() throws Exception {
		DualEventTester annotationTester = new DualEventTester();
		EventMethodTester annotationTester1 = new EventMethodTester();

		viewConfigurator.setupViewEngine(annotationTester);
		viewConfigurator.setupViewEngine(annotationTester1);

		engine.fireEvent(TestTypes.event, new ValueEvent<String>("air"));
		assertEquals("air", annotationTester.val1);
		assertEquals("air", annotationTester.val2);
		assertEquals("air", annotationTester1.val);

	}
}
