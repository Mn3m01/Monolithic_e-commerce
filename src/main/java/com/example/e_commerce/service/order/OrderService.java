package com.example.e_commerce.service.order;

import com.example.e_commerce.dto.order.CreateOrderRequest;
import com.example.e_commerce.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(CreateOrderRequest request);

    Order markOrderPaid(Long orderId);

    Order cancelOrder(Long orderId);

    Order getOrderById(Long orderId);

    List<Order> getOrdersForUser(Long userId);
}
