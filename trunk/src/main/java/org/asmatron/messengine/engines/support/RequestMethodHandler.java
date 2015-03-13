package org.asmatron.messengine.engines.support;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asmatron.messengine.action.ActionHandler;
import org.asmatron.messengine.action.RequestAction;
import org.asmatron.messengine.util.MethodInvoker;

public class RequestMethodHandler extends MethodInvoker implements ActionHandler<RequestAction<Object, Object>> {

    private final static Logger log = Logger.getLogger(RequestMethodHandler.class.getName());

    public RequestMethodHandler(Object object, Method method) {
        super(object, method);
        if (method.getParameterTypes().length > 1) {
            throw new IllegalMethodException("Illegal Engine binding on: " + object.getClass().getName() + "."
                    + method.getName());
        }
    }

    @Override
    public void handle(RequestAction<Object, Object> requestAction) {
        Object responseValue = null;
        RuntimeException callError = null;
        try {
            Object requestValue = requestAction.getValue();
            responseValue = invoke(requestValue);
        } catch (RuntimeException e) {
            callError = e;
        } catch (Exception e) {
            callError = new RuntimeException(e.getMessage(), e);
        }
        try {
            requestAction.getCallback().onResponse(responseValue);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        if (callError != null) {
            throw callError;
        }
    }

}
