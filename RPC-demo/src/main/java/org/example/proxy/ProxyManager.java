package org.example.proxy;

import org.example.call.CallerClient;
import org.example.common.InvokeProtocal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.UUID;

public class ProxyManager {

    public static Object getProxy(Class<?>[] interfaces, Class<?> targetClass) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, getInvocationHandler(targetClass));
    }

    private static InvocationHandler getInvocationHandler(Class<?> clazz) {
        String target = clazz.getName();
        return (proxy, method, args) -> {
            String methodName = method.getName();
            Type[] parameterTypes = method.getGenericParameterTypes();
            InvokeProtocal protocol = new InvokeProtocal(UUID.randomUUID().toString(), target, methodName, getParamTypes(parameterTypes), args);
            return CallerClient.getInstance().call(protocol);
        };
    }

    private static String[] getParamTypes(Type[] parameterTypes) {
        if (null == parameterTypes || parameterTypes.length <= 0) {
            return null;
        }
        String[] paramTypes = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            paramTypes[i] = parameterTypes[i].getTypeName();
        }
        return paramTypes;
    }
}
