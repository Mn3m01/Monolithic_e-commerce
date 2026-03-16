package com.example.e_commerce.mapper;

import com.example.e_commerce.dto.category.CategoryDTO;
import com.example.e_commerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);
}
