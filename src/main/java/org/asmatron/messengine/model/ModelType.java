package org.asmatron.messengine.model;

public class ModelType<T> {
	private final String id;
	private final boolean readOnly;

	public ModelType(String id, boolean readOnly) {
		this.id = id;
		this.readOnly = readOnly;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getId() {
		return id;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public boolean equals(Object obj) {
		ModelType<?> other = (ModelType<?>) obj;
		return this.id.equals(other.id);
	}

	@Override
	public String toString() {
		return "MODEL:" + id;
	}

	public static final <T> ModelType<T> readOnly(String id) {
		return new ModelType<T>(id, true);
	}

	public static final <T> ModelType<T> model(String id) {
		return new ModelType<T>(id, false);
	}

}
