package com.example.e_commerce.service.ImageService;

import com.example.e_commerce.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ImageService {
    Image uploadImage(MultipartFile multipartFile,Long productId) throws IOException;
    void deleteImage(String publicId) throws IOException;
    List<Image> getByProductId(Long productId);
}
