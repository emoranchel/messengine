package org.asmatron.messengine.engines.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asmatron.messengine.messaging.Message;

public class ResponseManager {

    private final static Logger log = Logger.getLogger(ResponseManager.class.getName());
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
                log.log(Level.SEVERE, "Unexpected exception executing response listener for type " + response.getType(), e);
            }
        }
    }

}
