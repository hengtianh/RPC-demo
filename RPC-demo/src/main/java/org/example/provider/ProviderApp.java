package org.example.provider;

import org.example.provider.community.InvokeManager;

public class ProviderApp {

    public static void main(String[] args) {
        InvokeManager invokeManager = new InvokeManager();
        invokeManager.start();
    }
}
