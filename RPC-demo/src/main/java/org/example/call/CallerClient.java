package org.example.call;

import com.ali.com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.example.common.InvokeProtocal;
import org.example.provider.codec.ProtocalDecoder;
import org.example.provider.codec.ProtocalEncoder;

import java.lang.reflect.InvocationHandler;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class CallerClient {

    private static ConcurrentHashMap<String, Object> rsMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Thread> threadMap = new ConcurrentHashMap<>();

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            2 * Runtime.getRuntime().availableProcessors() + 1,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            new ThreadFactoryBuilder().setThreadFactory(new DefaultThreadFactory("rpc caller threaad pool")).build()
    );

    private static Channel channel;

    private CallerClient() {
//        init();
    }

    public static CallerClient getInstance() {
        if (null != channel) {
            return new CallerClient();
        }
        synchronized (CallerClient.class) {
            if (null == channel) {
                channel = init();
            }
        }
        return new CallerClient();
    }

    private static Channel init() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocalEncoder()).addLast(new StringEncoder()).addLast(new ProtocalDecoder()).addLast(new DiscardServerHandler());
            }
        });
        ChannelFuture future = null;
        try {
            future = b.connect("127.0.0.1", 8080).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return future.channel();
    }

    public static void setResult(String requestId, Object result) {
//        synchronized (this) {
        rsMap.put(requestId, result);
        LockSupport.unpark(threadMap.get(requestId));
//            notifyAll();
//        }
    }

    /**
     * 整合异步调用，同步返回结果
     *
     * @param message
     * @return
     */
    public Object call(Object message) {
        // rpc通信
//        String requestId = sendMessage(init(), message);
//        CallerClient.init().writeAndFlush("aaa");
//        handler.setMessage(message);
//        Future future = pool.submit(handler);
        // 处理异步结果
//        synchronized (this) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        threadMap.put(requestId, Thread.currentThread());
//        LockSupport.park();
//        return rsMap.get(requestId);
//        try {
//            return future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
        DefaultFuture future = DefaultFuture.sent(channel, message);
        Object o = null;
        long l = 0;
        try {
            l = System.currentTimeMillis();
            o = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (o instanceof InvokeProtocal) {
            InvokeProtocal response = (InvokeProtocal) o;
            System.out.println("调用线程等待毫秒：" + (System.currentTimeMillis() - l));
            return response.result;
        }
        return null;
    }

    private static String sendMessage(Channel channel, Object message) {
        String requestId = UUID.randomUUID().toString();
        channel.attr(AttributeKey.valueOf("requestId")).set(requestId);
//        handler.setCallerClient(this);
        channel.writeAndFlush(message);
        return requestId;
    }

}
