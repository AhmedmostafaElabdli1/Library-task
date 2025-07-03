package com.code81.library.Mapper;

import com.code81.library.DTO.CategoryDTO;
import com.code81.library.Entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    // Map Category entity to CategoryDTO (including parentId)
    public static CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .subCategories(category.getSubCategories()
                        .stream()
                        .map(CategoryMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // Map from DTO to entity for create/update (no subcategories here)
    public static Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        // parentCategory should be set in service by parentId
        return category;
    }
}
