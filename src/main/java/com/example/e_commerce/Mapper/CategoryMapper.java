package com.example.e_commerce.Mapper;

import com.example.e_commerce.DTO.CategoryDTO;
import com.example.e_commerce.Entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);
}
