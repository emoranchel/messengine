package org.asmatron.messengine.app;

public class DefaultApplication<T> extends AbstractApplication<T> {
	private final Attributes attributes;

	public DefaultApplication(Module mainModule, Attributes attributes) {
		super(mainModule);
		this.attributes = attributes;
	}

	public DefaultApplication(Module mainModule) {
		this(mainModule, new DefaultAttributes());
	}

	public DefaultApplication() {
		this(new EmptyModule());
	}
	
	@Override
	protected Attributes getAttributes() {
		return attributes;
	}

}
