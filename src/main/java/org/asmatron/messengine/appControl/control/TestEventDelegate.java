package org.asmatron.messengine.appControl.control;


public class TestEventDelegate extends DefaultEventDelegate {
	public TestEventDelegate() {
		super(new TestingExecutorService());
	}
}
