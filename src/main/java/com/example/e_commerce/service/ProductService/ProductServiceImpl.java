package com.example.e_commerce.service.ProductService;

import com.example.e_commerce.dto.product.ProductDTO;
import com.example.e_commerce.entity.Category;
import com.example.e_commerce.entity.Product;
import com.example.e_commerce.mapper.ProductMapper;
import com.example.e_commerce.repository.CategoryRepo;
import com.example.e_commerce.repository.ProductRepo;
import com.example.e_commerce.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductMapper mapper;

    /*----------------------------------------------------------
                      ADD PRODUCT
     ----------------------------------------------------------*/
    @Override
    @Transactional
    public ProductDTO addProduct(ProductDTO dto) {
        // basic business validations
        if (dto.getPrice() == null || dto.getPrice().signum() <= 0) {
            throw new BusinessValidationException("Price must be greater than zero");
        }
        if (dto.getQuantity() != null && dto.getQuantity() < 0) {
            throw new BusinessValidationException("Quantity cannot be negative");
        }


        // map DTO -> entity (mapper will ignore category & images; we handle category here)
        Product product = mapper.toEntity(dto);

        // find or create category
        Category category = categoryRepo.findByName(dto.getCategoryName())
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName(dto.getCategoryName());
                    c.setDescription(dto.getCategoryDescription());
                    return categoryRepo.save(c);
                });

        product.setCategory(category);

        Product saved = productRepo.save(product);
        log.info("Product created: id={} name={}", saved.getId(), saved.getName());
        return mapper.toDTO(saved);
    }


    /*----------------------------------------------------------
                      UPDATE PRODUCT
     ----------------------------------------------------------*/
    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // business validations
        if (dto.getPrice() != null && dto.getPrice().signum() <= 0) {
            throw new BusinessValidationException("Price must be greater than zero");
        }
        if (dto.getQuantity() != null && dto.getQuantity() < 0) {
            throw new BusinessValidationException("Quantity cannot be negative");
        }

        // update simple fields if present
        mapper.updateEntityFromDTO(dto,existing);

        // update category if provided
        if (dto.getCategoryName() != null) {
            Category category = categoryRepo.findByName(dto.getCategoryName())
                    .orElseGet(() -> {
                        Category c = new Category();
                        c.setName(dto.getCategoryName());
                        c.setDescription(dto.getCategoryDescription());
                        return categoryRepo.save(c);
                    });
            existing.setCategory(category);
        }

        Product updated = productRepo.save(existing);
        log.info("Product updated: id={}", updated.getId());
        return mapper.toDTO(updated);
    }

    /*----------------------------------------------------------
                      DELETE PRODUCT BY ID
     ----------------------------------------------------------*/
    @Override
    @Transactional
    public void deleteProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepo.delete(product);
        log.info("Product deleted id={}", id);
    }

    /*----------------------------------------------------------
                      GET PRODUCT BY ID
     ----------------------------------------------------------*/
    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapper.toDTO(product);
    }

    /*----------------------------------------------------------
                      GET ALL PRODUCTS
     ----------------------------------------------------------*/
    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepo.findAll(pageable)
                .map(mapper::toDTO);
    }

    /*----------------------------------------------------------
                     GET PRODUCT BY NAME
     ----------------------------------------------------------*/
    @Override
    public List<ProductDTO> getProductsByName(String name) {
        return productRepo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /*----------------------------------------------------------
                    GET PRODUCT BY CATEGORY
     ----------------------------------------------------------*/
    @Override
    public List<ProductDTO> getProductsByCategory(String categoryName) {
        return productRepo.findByCategory_Name(categoryName)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }



}
