package org.asmatron.messengine.engines;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import org.asmatron.messengine.Callback;

import org.asmatron.messengine.EngineListener;
import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.ActionId;
import org.asmatron.messengine.action.ActionObject;
import org.asmatron.messengine.action.EmptyAction;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.action.ResponseCallback;
import org.asmatron.messengine.action.ValueAction;
import org.asmatron.messengine.engines.components.EngineStatus;
import org.asmatron.messengine.event.EmptyEvent;
import org.asmatron.messengine.event.EventId;
import org.asmatron.messengine.event.EventObject;
import org.asmatron.messengine.event.Listener;
import org.asmatron.messengine.event.ValueEvent;
import org.asmatron.messengine.messaging.Message;
import org.asmatron.messengine.messaging.MessageListener;
import org.asmatron.messengine.model.ModelId;

public class DefaultEngine implements Engine {

    private final ActionDelegate actionDelegate;
    private final EventDelegate eventDelegate;
    private final ModelDelegate modelDelegate;
    private final MessagingDelegate messagingDelegate;
    private EngineStatus status = EngineStatus.NEW;
    private final Set<EngineListener> listeners;

    public DefaultEngine() {
        this(new DefaultActionDelegate(), new DefaultEventDelegate(),
                new DefaultMessagingDelegate(), new DefaultModelDelegate());
    }

    protected DefaultEngine(ActionDelegate actionDelegate,
            EventDelegate eventDelegate, MessagingDelegate messagingDelegate,
            ModelDelegate modelDelegate) {
        this.actionDelegate = actionDelegate;
        this.eventDelegate = eventDelegate;
        this.messagingDelegate = messagingDelegate;
        this.modelDelegate = modelDelegate;
        this.listeners = new HashSet<EngineListener>();
    }

    @Override
    public <T extends ActionObject> void send(ActionId<T> type, T arg) {
        if (status != EngineStatus.STARTED) {
            throw new IllegalStateException();
        }
        actionDelegate.send(type.create(arg));
    }

    @Override
    public void send(ActionId<EmptyAction> type) {
        send(type, EmptyAction.INSTANCE);
    }

    @Override
    public <V, T> void request(ActionId<RequestAction<V, T>> type,
            V requestParameter, ResponseCallback<T> callback) {
        if (status != EngineStatus.STARTED) {
            throw new IllegalStateException();
        }
        actionDelegate.request(type, requestParameter, callback);
    }

    @Override
    public <T> void request(ActionId<RequestAction<Void, T>> type,
            ResponseCallback<T> callback) {
        if (status != EngineStatus.STARTED) {
            throw new IllegalStateException();
        }
        actionDelegate.request(type, null, callback);
    }

    @Override
    public <T extends EventObject> void fireEvent(EventId<T> eventType, T argument) {
        if (status != EngineStatus.STARTED) {
            throw new IllegalStateException();
        }
        eventDelegate.fireEvent(eventType, argument);
    }

    @Override
    public void fireEvent(EventId<EmptyEvent> type) {
        fireEvent(type, EmptyEvent.INSTANCE);
    }

    @Override
    public <T> T get(ModelId<T> type) {
        if (status == EngineStatus.NEW) {
            throw new IllegalStateException();
        }
        return modelDelegate.get(type);
    }

    @Override
    public <T> void set(ModelId<T> type, T value, EventId<ValueEvent<T>> event) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException();
        }
        modelDelegate.set(type, value);
        if (type.isReadOnly() && event != null) {
            throw new IllegalArgumentException(
                    "Setting a readonly should not throw events, they are \"session wide\" values.");
        }
        if (event != null) {
            value = modelDelegate.forceGet(type);
            if (status == EngineStatus.STARTED) {
                eventDelegate.fireEvent(event, new ValueEvent<T>(value));
            } else if (status == EngineStatus.NEW) {
                eventDelegate.fireLater(event, new ValueEvent<T>(value));
            }
        }
    }

    @Override
    public <T extends ActionObject> void addActionHandler(ActionId<T> actionType,
            ActionHandler<T> actionHandler) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException();
        }
        actionDelegate.addActionHandler(actionType, actionHandler);
    }

    @Override
    public <T extends ActionObject> void removeActionHandler(ActionId<T> action) {
        actionDelegate.removeActionHandler(action);
    }

    @Override
    public <T extends EventObject> void removeListener(EventId<T> type,
            Listener<T> listener) {
        eventDelegate.removeListener(type, listener);
    }

    @Override
    public <T extends EventObject> void addListener(EventId<T> type,
            Listener<T> listener) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException();
        }
        eventDelegate.addListener(type, listener);
    }

    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Callback callback) {
        for (EngineListener listener : listeners) {
            listener.onEngineStart();
        }
        status = EngineStatus.STARTED;
        actionDelegate.start();
        eventDelegate.start();
        messagingDelegate.start();
        modelDelegate.start();
        call(callback);
    }

    @Override
    public void stop() {
        stop(null);
    }

    @Override
    public void stop(Callback callback) {
        status = EngineStatus.STOPED;
        actionDelegate.stop();
        eventDelegate.stop();
        messagingDelegate.stop();
        modelDelegate.stop();
        for (EngineListener listener : listeners) {
            listener.onEngineStop();
        }
        call(callback);
    }

    @Override
    public <T> void fireValueEvent(EventId<ValueEvent<T>> type, T argument) {
        fireEvent(type, new ValueEvent<T>(argument));
    }

    @Override
    public <T> void sendValueAction(ActionId<ValueAction<T>> action, T argument) {
        send(action, new ValueAction<T>(argument));
    }

    @Override
    public void send(Message<?> message) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException();
        }
        messagingDelegate.send(message);
    }

    @Override
    public Future<Message<?>> request(Message<?> message, String responseType,
            long timeout) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException();
        }
        return messagingDelegate.request(message, responseType, timeout);
    }

    @Override
    public void addMessageListener(String type,
            MessageListener<? extends Message<?>> listener) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException();
        }
        messagingDelegate.addMessageListener(type, listener);
    }

    @Override
    public void removeMessageListener(String type,
            MessageListener<? extends Message<?>> listener) {
        messagingDelegate.removeMessageListener(type, listener);
    }

    @Override
    public void addEngineListener(EngineListener engineListener) {
        listeners.add(engineListener);
    }

    @Override
    public void removeEngineListener(EngineListener engineListener) {
        listeners.add(engineListener);
    }

    private void call(Callback callback) {
        if (callback != null) {
            try {
                callback.callback();
            } catch (Exception e) {

            }
        }
    }

}
