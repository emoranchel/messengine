package org.asmatron.messengine.i18n;


/**
 * Defines an internationalizable class that is aware of changes in language and
 * can react accordingly.
 * 
 */
public interface Internationalizable {
	/**
	 * Internationalizes the class this method is invoked automatically by the
	 * Messages instance IF:
	 * <ul>
	 * <li>The bean is added to the messages object using the Messages.add method</li>
	 * </ul>
	 * There are some details to consider.<br>
	 * <p>
	 * The internationalized class is responsible for its lifecycle, this means
	 * that the class will comonly not be candidate for garbage collection since
	 * it will still be alive on the messages object unless it specifically
	 * unbinds itself from the Messages object.
	 * </p>
	 * In short consider these rules: </b>
	 * <ul>
	 * <li><b>If the bean is created by spring and is a singleton bean</b> you
	 * need not to worry on garbage collection. The setMessages method will
	 * include a call to: messages.add(this); and that will suffice</li>
	 * <li><b>If you create the bean by hand you need to manage garbage
	 * collection.</b> the implementation of setMessages will include a call to:
	 * messages.add(this); and additionally you must provide a way to remove
	 * yourself from the internationalization context using messages.remove</li>
	 * </ul>
	 * <p>
	 * One quick trick. If you have a one shot internationalized bean then you can
	 * simplify things by implementing internationalization logic here and on the
	 * setMessages method just call this method. That way whenever you need to
	 * extend the class it will be more flexible.
	 * <p>
	 * 
	 * @param messages
	 */
	void internationalize(Messages messages);

	/**
	 * Set the messages object. this is a convenience method that might be used in
	 * Conjunction with the @Autowired annotation. <br>
	 * If you are not using the autowired annotation you can either use a one shot
	 * internationalization (using a call to internationalize()), or add yourself
	 * to the internationalization context using messages.add as well a way that
	 * as soon this bean is not used anymore call messages.remove.
	 * 
	 * @param messages
	 */
	void setMessages(Messages messages);
	
	/**
	 * 
	 * @param messages
	 */
	void removeMessages(Messages messages);
}
