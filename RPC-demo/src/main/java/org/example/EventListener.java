package org.example;

import com.ali.com.google.common.eventbus.Subscribe;

import java.util.concurrent.ConcurrentHashMap;

public class EventListener<T, P> {

    private final ConcurrentHashMap<T, ServiceHandler<T, P>> handlerMap = new ConcurrentHashMap<>();

    @Subscribe
    public void consumeMsg(MsgEvent<T, P> event) {
        P message = (P) (event.getIdentifier() + ":msg");
        event.setMessage(message);
        System.out.println(Thread.currentThread().getName() +": consume event: " + event.getMessage());
        ServiceHandler<T, P> handler = handlerMap.get(event.getIdentifier());
        if (handler == null) {
            return;
        }

        handler.getHandler().callback(handler.getServiceThread(), event.getMessage());
    }

    public void regist(ServiceHandler<T,P> serviceHandler) {
        handlerMap.put(serviceHandler.getIdentifier(), serviceHandler);
    }

}
