package com.example.e_commerce.controller;


import com.example.e_commerce.Entity.Image;
import com.example.e_commerce.Service.ImageService.ImageService;
import com.example.e_commerce.Service.ImageService.ImageServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/images")
public class ImageController {

    private final ImageService imageService;


    //Upload image and link to product
    @PostMapping("upload/{productId}")
    public ResponseEntity<Image> uploadImage(@PathVariable Long productId,
                                                   @RequestParam("file")MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(file,productId));
    }


    //Get all images for a product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Image>> getImageByProduct(@PathVariable Long productId){
        return ResponseEntity.ok(imageService.getByProductId(productId));
    }


    //Delete image by publicId
    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteImage(@PathVariable String publicId)throws IOException{
        imageService.deleteImage(publicId);
        return ResponseEntity.ok("Image deleted successfully");
    }

}
