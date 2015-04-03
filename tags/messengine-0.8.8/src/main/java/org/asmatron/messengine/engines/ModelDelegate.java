package org.asmatron.messengine.engines;

import org.asmatron.messengine.model.ModelId;

public interface ModelDelegate extends BaseDelegate {

  <T> T get(ModelId<T> type);

  <T> T forceGet(ModelId<T> type);

  <T> void set(ModelId<T> type, T value);

}
