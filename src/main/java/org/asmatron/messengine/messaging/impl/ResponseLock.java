package org.asmatron.messengine.messaging.impl;

import java.util.concurrent.Callable;

import org.asmatron.messengine.messaging.Message;


public class ResponseLock implements Callable<Message<?>> {

	private final String responseType;
	private final long timeout;
	private final Object lock;
	private Message<?> response;

	public ResponseLock(String responseType, long timeout) {
		this.responseType = responseType;
		this.timeout = timeout;
		this.lock = new Object();
	}

	@Override
	public Message<?> call() throws Exception {
		synchronized (lock) {
			try {
				lock.wait(timeout);
			} catch (InterruptedException e) {
			}
		}
		return response;
	};

	public void release(Message<?> response) {
		try {
			this.response = response;
			synchronized (lock) {
				lock.notify();
			}
		} catch (Exception e) {
		}
	}

	
	public String getResponseType() {
		return responseType;
	}
	
}
