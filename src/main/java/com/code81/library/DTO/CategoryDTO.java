package com.code81.library.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;  // optional for parent ref

    // For GET responses: recursive subcategories
    private List<CategoryDTO> subCategories;
}
