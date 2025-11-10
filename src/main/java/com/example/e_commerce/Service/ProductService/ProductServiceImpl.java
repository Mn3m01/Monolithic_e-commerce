package com.example.e_commerce.Service.ProductService;
import com.example.e_commerce.Entity.Category;
import com.example.e_commerce.Entity.Product;
import com.example.e_commerce.Repository.CategoryRepo;
import com.example.e_commerce.Repository.ProductRepo;
import com.example.e_commerce.DTO.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;


    @Override
    public Product addProduct(ProductDTO productDTO) {
        Category category = categoryRepo.findByName(productDTO.getCategoryName())
                .orElseGet(()->{
                    Category newCategory = new Category();
                    newCategory.setName(productDTO.getCategoryName());
                    return categoryRepo.save(newCategory);
                });
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .category(category)
                .build();

        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepo.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setDescription(product.getDescription());
                    existing.setPrice(product.getPrice());
                    existing.setQuantity(product.getQuantity());
                    existing.setCategory(product.getCategory());
                    //existing.setImages(product.getImages());
                    log.info("Updated product with id {}", id);
                    return productRepo.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product Not Found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found with id : " + id));
        productRepo.delete(product);
        productRepo.flush();
    }


    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id).orElseThrow(()-> new RuntimeException("Product Not Found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> getProductByName(String name) {

        return productRepo.findByName(name);
    }

    @Override
    public List<Product> getProductByCategory(String categoryName) {
        return productRepo.findByCategory_Name(categoryName);
    }


    @Transactional
    public void deleteByName(String name) {
        productRepo.deleteByName(name);
    }

    public ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .imagesUrls(product.getImages() != null
                        ? product.getImages().stream()
                        .map(image -> image.getImageUrl())
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
