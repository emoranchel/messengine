package org.asmatron.messengine.observ;

public class ObservePropertyChanged<S, V> extends ObservValue<V> {

  private final S source;
  private final V oldValue;

  public ObservePropertyChanged(S s, V newValue, V oldValue) {
    super(newValue);
    this.oldValue = oldValue;
    this.source = s;
  }

  public V getOldValue() {
    return oldValue;
  }

  public S getSource() {
    return source;
  }

}
