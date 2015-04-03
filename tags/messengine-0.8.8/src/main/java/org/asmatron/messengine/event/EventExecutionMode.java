package org.asmatron.messengine.event;

public enum EventExecutionMode {

  /**
   * NORMAL BLOCKING CALL, no other events will launch until this finishes
   */
  NORMAL,
  /**
   * ASYNC NONBLOCKING CALL, this event handler deploys on a separate thread
   */
  ASYNC,
  /**
   * SWING "NONBLOCKING" CALL, this call will execute on swing will not block
   * event handlers but will take the swing event dispatch thread.
   */
  ASYNC_IN_SWING,
  /**
   * SWING BLOCKING CALL, this call will execute on swing using the swing event
   * dispatch thread.
   */
  NORMAL_IN_SWING
}
