package org.asmatron.messengine.event;

public class EmptyEvent extends EventObject {
	public static final EmptyEvent INSTANCE = new EmptyEvent();

	private EmptyEvent() {
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		return true;
	}

}
