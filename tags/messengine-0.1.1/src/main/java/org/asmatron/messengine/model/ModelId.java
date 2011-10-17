package org.asmatron.messengine.model;

public class ModelId<T> {
	private final String id;
	private final boolean readOnly;

	public ModelId(String id, boolean readOnly) {
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
		ModelId<?> other = (ModelId<?>) obj;
		return this.id.equals(other.id);
	}

	@Override
	public String toString() {
		return "MODEL:" + id;
	}

	public static final <T> ModelId<T> readOnly(String id) {
		return new ModelId<T>(id, true);
	}

	public static final <T> ModelId<T> model(String id) {
		return new ModelId<T>(id, false);
	}

}
