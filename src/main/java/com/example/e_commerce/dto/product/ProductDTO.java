package com.example.e_commerce.dto.product;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {


    @Null(message = "ID must not be provided, it is auto-generated")
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String categoryDescription;

    private List<String> imagesUrls;
}
