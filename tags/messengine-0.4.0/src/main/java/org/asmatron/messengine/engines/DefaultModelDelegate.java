package org.asmatron.messengine.engines;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asmatron.messengine.engines.components.EngineStatus;
import org.asmatron.messengine.model.ModelId;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultModelDelegate implements ModelDelegate {
    private static final String ERROR_ACCESS = "Model is accessed but is not yet set, this call will block until value is set or engine is shutdown.";
    private final static Logger log = Logger.getLogger(ModelId.class.getName());
    private Map model;
    private EngineStatus status = EngineStatus.NEW;

    public DefaultModelDelegate() {
        model = new HashMap();
    }

    @Override
    public <T> T get(ModelId<T> type) {
        if (status != EngineStatus.STARTED) {
            throw new IllegalStateException("Model cannot be used until started, currently:" + status);
        }
        T value = (T) model.get(type);
        if (type.isReadOnly() && value == null) {
            log.log(Level.WARNING, ERROR_ACCESS, new IllegalStateException(ERROR_ACCESS));
            throw new NullPointerException("model not set, should be set before engine starts!");
        }
        if (value instanceof Collection) {
            Collection copy;
            try {
                copy = (Collection) value.getClass().newInstance();
                copy.addAll((Collection) value);
                return (T) copy;
            } catch (Exception e) {
                return null;
            }
        }
        return value;
    }

    @Override
    public <T> void set(ModelId<T> type, T value) {
        if (status == EngineStatus.STOPED) {
            throw new IllegalStateException("Engine stoped cannot set model now." + status);
        }
        if (type.isReadOnly() && model.get(type) != null) {
            throw new IllegalStateException("Model already set, " + type + " should only be set once!");
        }
        if (value == null) {
            model.remove(type);
        } else {
            model.put(type, value);
        }
    }

    @Override
    public void start() {
        status = EngineStatus.STARTED;
    }

    @Override
    public synchronized void stop() {
        status = EngineStatus.STOPED;
    }

    @Override
    public <T> T forceGet(ModelId<T> type) {
        return (T) model.get(type);
    }

}
