package org.asmatron.messengine.app;

/**
 * A phase is a simple process that runs linearly and is controlled by an
 * Application.<br>
 * 
 * The idea is that a phase runs and does some work optionally will put more
 * phases in the Application and at the end of its execution just returns and
 * then the next phase would be launched. <br>
 * 
 * If threading this phase is required or if a window is shown and we require
 * user to close the window to continue the phases then we need to add some
 * manual JooJoo This is easily done using either semaphores or old java.
 * 
 * Example:
 * 
 * <code>
 * public void doPhase(){
 *   final Object lock = new Object();
 *   JFrame frame = new JFrame();
 *   frame.setVisible(true);
 *   ...
 *   frame.addWindowListener(new WindowAdapter(){
 *     public void windowClosed(WindowEvent evt){
 *       synchronized(lock){
 *         lock.notifyAll();
 *       }
 *     }
 *   });
 *   synchronized(lock){
 *     try{
 *       lock.wait();
 *     }catch(InterruptedException e){}
 *   }
 * }
 * </code>
 * 
 * Or same shit less code:
 * 
 * <code>
 * public void doPhase(){
 *   final Semaphore lock = new Semaphore(0);
 *   JFrame frame = new JFrame();
 *   frame.setVisible(true);
 *   ...
 *   frame.addWindowListener(new WindowAdapter(){
 *     public void windowClosed(WindowEvent evt){
 *         semaphore.release();
 *     }
 *   });
 *   try{
 *     semaphore.aquire();
 *   }catch(InterruptedException e){}
 * }
 * </code>
 * 
 */
public interface AppModule {

	void load();

	void activate();

	void destroy();

}
