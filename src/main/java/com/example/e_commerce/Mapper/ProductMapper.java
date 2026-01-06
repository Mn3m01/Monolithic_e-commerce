package com.example.e_commerce.Mapper;

import com.example.e_commerce.DTO.ProductDTO;
import com.example.e_commerce.Entity.Category;
import com.example.e_commerce.Entity.Image;
import com.example.e_commerce.Entity.Product;
import org.mapstruct.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    /* ============================================================
       =============== ENTITY -> DTO MAPPING ======================
       ============================================================ */

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.description", target = "categoryDescription")
    @Mapping(target = "imagesUrls", expression = "java(mapImagesToUrls(product.getImages()))")
    ProductDTO toDTO(Product product);

    /* ============================================================
       =============== DTO -> ENTITY MAPPING ======================
       ============================================================ */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductDTO dto);

    /* ============================================================
       =============== UPDATE ENTITY FROM DTO =====================
       ============================================================ */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product product);


    /* ============================================================
       ============ HELPERS FOR CUSTOM MAPPINGS ===================
       ============================================================ */

    default List<String> mapImagesToUrls(List<Image> images) {
        if (images == null) return null;

        return images.stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
    }

    default Category mapCategoryFromName(String name, String description) {
        if (name == null) return null;

        Category c = new Category();
        c.setName(name);
        c.setDescription(description);
        return c;
    }
}
