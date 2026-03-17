package com.example.e_commerce.repository;

import com.example.e_commerce.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Set;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    List<Inventory> findAllByProductIdIn(Set<Long> productIds);
}
