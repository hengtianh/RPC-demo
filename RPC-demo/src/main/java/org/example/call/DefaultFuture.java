package org.example.call;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.example.common.InvokeProtocal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DefaultFuture extends CompletableFuture<Object> {

    private static final Map<String, DefaultFuture> FUTURES = new HashMap<>();

    public static DefaultFuture sent(Channel channel, Object request) {
        if (!(request instanceof InvokeProtocal)) {
            return null;
        }
        InvokeProtocal invokeProtocal = (InvokeProtocal) request;
        DefaultFuture future = new DefaultFuture();
        FUTURES.put(invokeProtocal.requestId, future);
        ChannelFuture channelFuture = channel.writeAndFlush(request);
        return future;
    }

    public static DefaultFuture get(String requestId) {
        return FUTURES.get(requestId);
    }
}
