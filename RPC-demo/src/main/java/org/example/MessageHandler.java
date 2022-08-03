package org.example;

@FunctionalInterface
public interface MessageHandler<T> {

    void handle(Thread thread, T result);

}
