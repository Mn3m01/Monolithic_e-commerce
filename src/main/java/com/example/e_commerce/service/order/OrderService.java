package com.example.e_commerce.service.order;

import com.example.e_commerce.dto.order.CreateOrderRequest;
import com.example.e_commerce.dto.order.OrderItemRequest;
import com.example.e_commerce.entity.*;
import com.example.e_commerce.exception.BusinessValidationException;
import com.example.e_commerce.exception.ResourceNotFoundException;
import com.example.e_commerce.repository.InventoryRepo;
import com.example.e_commerce.repository.OrderRepo;
import com.example.e_commerce.repository.ProductRepo;
import com.example.e_commerce.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;

    public Order createOrder(CreateOrderRequest request) {

        // 1️⃣ merge duplicate productIds
        Map<Long, Integer> productQuantities = new HashMap<>();
        for (OrderItemRequest item : request.getItems()) {
            productQuantities.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }

        // 2️⃣ grep all products in once 
        List<Product> products = productRepo.findAllById(productQuantities.keySet());

        if (products.size() != productQuantities.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        // 3️⃣ Inventory check up
        Map<Long, Inventory> inventoryMap = inventoryRepo.findAllByProductIdIn(productQuantities.keySet())
                .stream()
                .collect(Collectors.toMap(Inventory::getProductId, inv -> inv));

        for (Product p : products) {
            int requestedQty = productQuantities.get(p.getId());
            Inventory inv = inventoryMap.get(p.getId());
            int available = inv.getTotalQuantity() - inv.getReservedQuantity();
            if (requestedQty > available) {
                throw new BusinessValidationException(
                        "Insufficient stock for product " + p.getName()
                );
            }
        }

        // 4️⃣ reservation in Inventory
        for (Product p : products) {
            int requestedQty = productQuantities.get(p.getId());
            Inventory inv = inventoryMap.get(p.getId());
            inv.setReservedQuantity(inv.getReservedQuantity() + requestedQty);
            inventoryRepo.save(inv); // optimistic locking ensures no overselling
        }

        // 5️⃣ Order creation
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 6️⃣ OrderItems creation with snapshot
        for (Product p : products) {
            int qty = productQuantities.get(p.getId());
            BigDecimal subtotal = p.getPrice().multiply(BigDecimal.valueOf(qty));

            OrderItem item = OrderItem.builder()
                    .productId(p.getId())
                    .productName(p.getName())
                    .priceAtPurchase(p.getPrice())
                    .quantity(qty)
                    .subtotal(subtotal)
                    .order(order)
                    .build();

            orderItems.add(item);
            totalPrice = totalPrice.add(subtotal);
        }

        // 7️⃣ save
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepo.save(order);

        log.info("Order created id={} totalPrice={}", savedOrder.getId(), savedOrder.getTotalPrice());

        return savedOrder;
    }
}
