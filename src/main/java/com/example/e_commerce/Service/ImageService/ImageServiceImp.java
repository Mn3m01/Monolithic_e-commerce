package com.example.e_commerce.Service.ImageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.e_commerce.Entity.Image;
import com.example.e_commerce.Entity.Product;
import com.example.e_commerce.Repository.ImageRepo;
import com.example.e_commerce.Repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImp implements ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepo imageRepo;
    private final ProductRepo productRepo;

    @Override
    public Image uploadImage(MultipartFile file, Long productId) throws IOException {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "ecommerce/products"));

        Image image = Image.builder()
                .imageUrl(uploadResult.get("url").toString())
                .publicId(uploadResult.get("public_id").toString())
                .product(product)
                .build();

        return imageRepo.save(image);
    }

    @Transactional
    @Override
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        imageRepo.deleteByPublicId(publicId);
    }

    @Override
    public List<Image> getByProductId(Long productId) {
        return imageRepo.findByProductId(productId);
    }
}
