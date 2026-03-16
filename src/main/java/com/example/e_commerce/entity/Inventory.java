package com.example.e_commerce.entity;

import jakarta.persistence.*;


@Entity
public class Inventory {
    @Id
    private Long productId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    private int totalQuantity;

    private int reservedQuantity;

    @Version
    private Long version;
}
