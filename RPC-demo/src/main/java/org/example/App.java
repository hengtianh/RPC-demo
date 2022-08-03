package org.example;

import com.ali.com.google.common.eventbus.EventBus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.call.CallerClient;
import org.example.call.DiscardServerHandler;
import org.example.call.InvokeManager;
import org.example.common.InvokeProtocal;
import org.example.provider.service.OrderService;
import org.example.provider.service.impl.OrderServiceImpl;

/**
 * Hello world!
 */
public class App {
    static EventBus eventBus = new EventBus();

    static EventListener listener = new EventListener();

    static OrderService orderService = new OrderServiceImpl();

    static {
        eventBus.register(listener);
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        final ExpenseService service = new ExpenseService(listener, orderService);
        final String orderId = "order";
        for (int i = 0; i < 5; i++) {
            Woker woker = new Woker(service, orderId + i);
            woker.start();
        }
        for (int i = 0; i < 0; i++) {
            Notifier notifier = new Notifier(orderId + i);
            notifier.start();
        }
//        new InvokeManager().start();
//        InvokeProtocal protocal = new InvokeProtocal("org.example.provider.service.impl.OrderServiceImpl", "createOrderId", new String[]{"java.lang.String"}, new String[]{"order1"});
//        channel.writeAndFlush("123");
//        channel.writeAndFlush(protocal);

    }

    static class Woker extends Thread {

        ExpenseService service;

        String orderId;

        public Woker(ExpenseService service, String orderId) {
            this.service = service;
            this.orderId = orderId;
        }

        @Override
        public void run() {
            String checkExpense = service.checkExpense(orderId);
            System.out.println(Thread.currentThread().getName() +": expense result: " + checkExpense);
        }
    }

    static class Notifier extends Thread {

        private String orderId;

        public Notifier(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("sleeping ...");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            eventBus.post(new MsgEvent(null, orderId));
        }
    }
}
