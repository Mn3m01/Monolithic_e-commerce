package com.example.e_commerce.DTO;

import com.example.e_commerce.Entity.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @Null(message = "ID must not be provided, it is auto-generated")
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Size(max = 1000,message = "Description must not exceed 1000 characters")
    private String description;

    private List<Product> products;

}
