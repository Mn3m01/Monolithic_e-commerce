package com.example.e_commerce.repository;

import com.example.e_commerce.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image,Long> {
    List<Image> findByProductId(Long productId);
    void deleteByPublicId(String publicId);
}
