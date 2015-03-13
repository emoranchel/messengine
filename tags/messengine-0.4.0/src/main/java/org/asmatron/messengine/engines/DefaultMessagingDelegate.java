package org.asmatron.messengine.engines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.asmatron.messengine.engines.components.MessageConsumer;
import org.asmatron.messengine.engines.components.ResponseLock;
import org.asmatron.messengine.engines.components.ResponseManager;
import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultMessagingDelegate implements MessagingDelegate, Runnable {

    private final static Logger log = Logger.getLogger(Message.class.getName());
    private final BlockingQueue<Message<?>> queue;
    private final ExecutorService messageExecutor;
    private final ExecutorService engineExecutor;
    private final Map<String, List<MessageListener<? extends Message<?>>>> listeners;
    private final AtomicBoolean shuttingDown;
    private final ResponseManager responseManager;

    public DefaultMessagingDelegate() {
        queue = new LinkedBlockingQueue<Message<?>>();
        messageExecutor = Executors.newCachedThreadPool();
        engineExecutor = Executors.newSingleThreadExecutor();
        listeners = new HashMap<String, List<MessageListener<? extends Message<?>>>>();
        responseManager = new ResponseManager();
        shuttingDown = new AtomicBoolean(false);
    }

    @PostConstruct
    public void start() {
        responseManager.initialize();
        engineExecutor.execute(this);
    }

    @PreDestroy
    public void stop() {
        shuttingDown.set(true);
        messageExecutor.shutdownNow();
        responseManager.shutdown();
        engineExecutor.shutdownNow();
        shuttingDown.set(false);
        log.info("MessEngine has shutdown...");
    }

    @Override
    public void send(Message<?> message) {
        if (message.getType() == null) {
            throw new IllegalArgumentException("The Type is invalid.");
        }
        queueMessage(message);
    }

    @Override
    public Future<Message<?>> request(Message<?> message, String responseType, long timeout) {
        if (message.getType() == null) {
            throw new IllegalArgumentException("The Type is invalid.");
        }
        Future<Message<?>> future = responseManager.addResponseListener(new ResponseLock(responseType, timeout));
        queueMessage(message);
        return future;
    }

    private void queueMessage(Message<?> message) {
        queue.offer(message);
    }

    @Override
    public void run() {
        try {
            while (!engineExecutor.isShutdown()) {
                Message<?> message = queue.take();
                String type = message.getType();
                if (listeners.containsKey(type)) {
                    messageExecutor.execute(new MessageConsumer(message, listeners.get(type)));
                }
                responseManager.notifyResponse(message);
            }
        } catch (InterruptedException e) {
            if (!shuttingDown.get()) {
                log.log(Level.WARNING, "MessEngine queue was interrupted.");
            }
        }
    }

    @Override
    public void addMessageListener(String type, MessageListener<? extends Message<?>> listener) {
        List<MessageListener<? extends Message<?>>> list = listeners.get(type);
        if (list == null) {
            list = new CopyOnWriteArrayList<MessageListener<? extends Message<?>>>();
            listeners.put(type, list);
        }
        list.add(listener);
    }

    @Override
    public void removeMessageListener(String type, MessageListener<? extends Message<?>> listener) {
        if (listeners.containsKey(type)) {
            listeners.get(type).remove(listener);
        }
    }
}
