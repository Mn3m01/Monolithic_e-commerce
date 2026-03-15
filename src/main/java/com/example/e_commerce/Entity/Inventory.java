package com.example.e_commerce.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;


@Entity
public class Inventory {
    @Id
    private Long productId;

    private int quantity;

    @Version
    private Long version;
}
