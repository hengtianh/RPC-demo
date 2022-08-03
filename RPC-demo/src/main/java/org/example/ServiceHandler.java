package org.example;

public class ServiceHandler<T, P> {

    private final T identifier;

    private final Thread serviceThread;

    private final AbstractMessageHandler<P> handler;

    public ServiceHandler(T identifier, Thread serviceThread, AbstractMessageHandler<P> handler) {
        this.identifier = identifier;
        this.serviceThread = serviceThread;
        this.handler = handler;
    }

    public T getIdentifier() {
        return identifier;
    }

    public Thread getServiceThread() {
        return serviceThread;
    }

    public AbstractMessageHandler<P> getHandler() {
        return handler;
    }
}
