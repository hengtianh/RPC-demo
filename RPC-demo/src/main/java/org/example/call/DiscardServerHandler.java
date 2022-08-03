package org.example.call;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.example.common.InvokeProtocal;

import java.util.concurrent.Callable;

public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> implements Callable {

    private ChannelHandlerContext ctx;

    private Object message;

    private Object response;

    private CallerClient callerClient;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        System.out.println(msg);
//        response = msg;
        if (msg instanceof InvokeProtocal) {
            InvokeProtocal invocation = (InvokeProtocal) msg;
            DefaultFuture future = DefaultFuture.get(invocation.requestId);
            Thread.sleep(3000);
            future.complete(msg);
        }
//        CallerClient.setResult((String) channelHandlerContext.channel().attr(AttributeKey.valueOf("requestId")).get(), msg);
//        notifyAll();
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelActive(channelHandlerContext);
        this.ctx = channelHandlerContext;
    }

    @Override
    public Object call() throws Exception {
        ctx.writeAndFlush(message);
        synchronized (this) {
            wait();
        }
        return response;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public void setCallerClient(CallerClient client) {
        this.callerClient = client;
    }
}
