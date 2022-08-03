package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractMessageHandler<T> implements MessageHandler<T> {

    private final ConcurrentHashMap<Thread, T> map = new ConcurrentHashMap<>();

    public void callback(Thread thread, T result) {
        handle(thread, result);
        LockSupport.unpark(thread);
    }

    @Override
    public void handle(Thread thread, T result) {
        map.put(thread, (T) (thread.getName() + ":" +result));
    }

    public ConcurrentHashMap<Thread, T> getMap() {
        return map;
    }
}
