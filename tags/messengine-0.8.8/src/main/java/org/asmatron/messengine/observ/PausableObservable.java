package org.asmatron.messengine.observ;

public class PausableObservable<T extends ObserveObject> extends Observable<T> implements ObservableControl {

  private boolean enabled = true;
  private T suspendedEventObject = null;

  @Override
  public void suspend() {
    enabled = false;
  }

  @Override
  public void resume() {
    enabled = true;
    fire(suspendedEventObject);
    suspendedEventObject = null;
  }

  @Override
  public boolean fire(T param) {
    if (enabled) {
      return super.fire(param);
    } else {
      suspendedEventObject = param;
      return true;
    }
  }

  ;

	public boolean isEnabled() {
    return true;
  }
}
