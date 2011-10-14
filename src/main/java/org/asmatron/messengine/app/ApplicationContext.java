package org.asmatron.messengine.app;

public interface ApplicationContext {
	public <T> T getBean(Class<T> clasz);

	public <T> T getBean(String name, Class<T> clasz);

	public <T> T getBean(String name);

	public void close();
}
