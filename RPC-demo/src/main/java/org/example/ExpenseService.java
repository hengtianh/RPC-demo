package org.example;

import org.example.provider.service.OrderService;
import org.example.provider.service.dto.OrderDTO;
import org.example.proxy.ProxyManager;

public class ExpenseService extends AbstractMessageHandler<String> {

    private EventListener listener;

    private OrderService orderService;

    public ExpenseService(EventListener listener, OrderService orderService) {
        this.listener = listener;
        this.orderService = orderService;
    }

    public String checkExpense(String orderId) {
        ServiceHandler<String, String> serviceHandler = new ServiceHandler<>(orderId, Thread.currentThread(), this);
        listener.regist(serviceHandler);
        return check();
//        if (!check) {
//            LockSupport.park();
//            return getMap().get(Thread.currentThread());
//        }
//        return "normal result";
    }

    public String check() {
        try {
            System.out.println("rpc processing ...");
            OrderService proxy = (OrderService) ProxyManager.getProxy(orderService.getClass().getInterfaces(), orderService.getClass());
            OrderDTO order = proxy.createOrderId("orderId123");
//            System.out.println("result:" + order);
            Thread.sleep(1);
            return order.getUuid();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
