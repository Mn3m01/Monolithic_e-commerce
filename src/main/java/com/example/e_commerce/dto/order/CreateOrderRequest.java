package com.example.e_commerce.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {
    @NotNull
    private Long userId;

    @NotEmpty
    private List<OrderItemRequest> items;
}
