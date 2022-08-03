package org.example;

public class MsgEvent<T, P> {
    private P message;

    private T identifier;

    public MsgEvent(P message, T orderId) {
        this.message = message;
        this.identifier = orderId;
    }

    public void setMessage(P message) {
        this.message = message;
    }

    public P getMessage() {
        return message;
    }

    public T getIdentifier() {
        return identifier;
    }
}
