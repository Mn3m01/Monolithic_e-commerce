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
public class OrderServiceImpl implements OrderService {

    private final ProductRepo productRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;

    @Override
    public Order createOrder(CreateOrderRequest request) {

        // merge duplicate productIds
        Map<Long, Integer> productQuantities = new HashMap<>();
        for (OrderItemRequest item : request.getItems()) {
            productQuantities.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }

        // get all products at once
        List<Product> products = productRepo.findAllById(productQuantities.keySet());

        if (products.size() != productQuantities.size()) {
            throw new ResourceNotFoundException("One or more products not found");
        }

        // Inventory check
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

        // reserve inventory
        for (Product p : products) {
            int requestedQty = productQuantities.get(p.getId());
            Inventory inv = inventoryMap.get(p.getId());
            inv.setReservedQuantity(inv.getReservedQuantity() + requestedQty);
            inventoryRepo.save(inv); // optimistic locking ensures no overselling
        }

        // Order creation
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        // OrderItems creation with snapshot
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

        // save
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepo.save(order);

        log.info("Order created id={} totalPrice={}", savedOrder.getId(), savedOrder.getTotalPrice());

        return savedOrder;
    }

    @Override
    public Order markOrderPaid(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessValidationException("Only orders pending payment can be marked as paid");
        }

        // finalize inventory: move reserved stock to sold (reduce total and reserved)
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepo.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product " + item.getProductId()));

            int qty = item.getQuantity();
            if (inventory.getReservedQuantity() < qty || inventory.getTotalQuantity() < qty) {
                throw new BusinessValidationException("Inconsistent inventory state for product " + item.getProductId());
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() - qty);
            inventory.setTotalQuantity(inventory.getTotalQuantity() - qty);
            inventoryRepo.save(inventory);
        }

        order.setStatus(OrderStatus.PAID);
        Order saved = orderRepo.save(order);
        log.info("Order marked as PAID id={}", saved.getId());
        return saved;
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessValidationException("Only orders pending payment can be cancelled");
        }

        // release inventory reservations
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepo.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product " + item.getProductId()));

            int qty = item.getQuantity();
            if (inventory.getReservedQuantity() < qty) {
                throw new BusinessValidationException("Inconsistent inventory reservation for product " + item.getProductId());
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() - qty);
            inventoryRepo.save(inventory);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepo.save(order);
        log.info("Order cancelled id={}", saved.getId());
        return saved;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<Order> getOrdersForUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        return orderRepo.findAllByUser_Id(userId);
    }
}
