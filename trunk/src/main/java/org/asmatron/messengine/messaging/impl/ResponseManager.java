package org.asmatron.messengine.messaging.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asmatron.messengine.messaging.Message;


public class ResponseManager {

	private final Log log = LogFactory.getLog(this.getClass());
	private Map<String, ResponseLock> listeners;
	private ExecutorService executor;

	public void initialize() {
		listeners = Collections.synchronizedMap(new HashMap<String, ResponseLock>());
		executor = Executors.newCachedThreadPool();
	}

	public void shutdown() {
		executor.shutdownNow();
	}

	public Future<Message<?>> addResponseListener(ResponseLock listener) {
		if (listeners.containsKey(listener.getResponseType())) {
			throw new IllegalStateException("There is already a response listener for this response type: "
					+ listener.getResponseType());
		}
		listeners.put(listener.getResponseType(), listener);
		return executor.submit(listener);
	}

	public void notifyResponse(Message<?> response) {
		ResponseLock listener = listeners.remove(response.getType());
		if (listener != null) {
			try {
				listener.release(response);
			} catch (Exception e) {
				log.error("Unexpected exception executing response listener for type " + response.getType(), e);
			}
		}
	}

}
