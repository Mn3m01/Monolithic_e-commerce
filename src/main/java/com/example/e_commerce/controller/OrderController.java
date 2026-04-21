package com.example.e_commerce.controller;

import com.example.e_commerce.dto.order.CreateOrderRequest;
import com.example.e_commerce.entity.Order;
import com.example.e_commerce.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Order> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markOrderPaid(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersForUser(userId));
    }
}

