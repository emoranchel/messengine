package org.asmatron.messengine.action;

public class ActionId<T extends ActionObject> {

  private final String id;

  public ActionId(String id) {
    this.id = id;
  }

  public Action<T> create(T param) {
    return new Action<T>(this, param);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    ActionId<?> other = (ActionId<?>) obj;
    return this.id.equals(other.id);
  }

  @Override
  public String toString() {
    return "ACTION:" + id;
  }

  public static final <T extends ActionObject> ActionId<T> cm(String id) {
    return new ActionId<T>(id);
  }
}
