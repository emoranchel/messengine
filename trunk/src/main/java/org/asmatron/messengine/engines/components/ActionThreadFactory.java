package org.asmatron.messengine.engines.components;

import java.util.concurrent.ThreadFactory;

public class ActionThreadFactory implements ThreadFactory {

  private int count = 0;

  @Override
  public Thread newThread(Runnable r) {
    count++;
    Thread t = new Thread(r);
    if (r instanceof ActionRunnable) {
      t.setName(((ActionRunnable) r).getName() + "[" + count + "]");
    } else {
      t.setName("ActionRunner[" + count + "]");
    }
    return t;
  }

}
