package org.example.provider.service.impl;

import org.example.provider.service.OrderService;
import org.example.provider.service.dto.OrderDTO;

import java.util.Date;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    @Override
    public OrderDTO createOrderId(String orderId) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setUuid(UUID.randomUUID().toString());
        orderDTO.setGmtCreate(new Date());
        System.out.println("service create " + orderDTO);
        return orderDTO;
    }
}
