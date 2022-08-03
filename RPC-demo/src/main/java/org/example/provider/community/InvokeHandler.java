package org.example.provider.community;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.common.InvokeProtocal;
import org.example.provider.ProviderApp;

import java.lang.reflect.Method;

public class InvokeHandler extends SimpleChannelInboundHandler<InvokeProtocal> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, InvokeProtocal protocal) throws Exception {
//        System.out.println(protocal);
        Class<?> target = Class.forName(protocal.target, true, ProviderApp.class.getClassLoader());
        Method method = target.getDeclaredMethod(protocal.methodName, getParamTypes(protocal.paramTypes));
        Object result = method.invoke(target.newInstance(), getArgs(protocal.args));
        protocal.result = result;
        channelHandlerContext.writeAndFlush(protocal);
    }

    private Class[] getParamTypes(String[] paramTypes) {
        if (null == paramTypes || paramTypes.length <= 0) {
            return null;
        }
        Class[] typeClasses = new Class[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            try {
                Class<?> aClass = Class.forName(paramTypes[i], true, ProviderApp.class.getClassLoader());
                typeClasses[i] = aClass;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return typeClasses;
    }

    private Object[] getArgs(Object[] args) {
        if (null == args || args.length <= 0) {
            return null;
        }
        return args;
    }
}
