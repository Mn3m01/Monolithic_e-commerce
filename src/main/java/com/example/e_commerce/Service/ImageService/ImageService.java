package com.example.e_commerce.Service.ImageService;

import com.example.e_commerce.Entity.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiFileChooserUI;
import java.io.IOException;
import java.util.List;


public interface ImageService {
    Image uploadImage(MultipartFile multipartFile,Long productId) throws IOException;
    void deleteImage(String publicId) throws IOException;
    List<Image> getByProductId(Long productId);
}
