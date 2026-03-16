package com.example.e_commerce.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
