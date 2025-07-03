package com.code81.library.Controller;

import com.code81.library.DTO.CategoryDTO;
import com.code81.library.Entity.Category;
import com.code81.library.Mapper.CategoryMapper;
import com.code81.library.Service.CategoryService;
import com.code81.library.Util.UserActivityLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserActivityLogger userActivityLogger;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> rootCategories = categoryService.getAllRootCategories();
        List<CategoryDTO> dtos = rootCategories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
        userActivityLogger.log("Viewed all categories");
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO dto) {
        Category category = CategoryMapper.toEntity(dto);
        Category created = categoryService.create(category, dto.getParentId());
        userActivityLogger.log("Created category: " + created.getName());
        return ResponseEntity.ok(CategoryMapper.toDTO(created));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        userActivityLogger.log("Viewed category with ID: " + id);
        return ResponseEntity.ok(CategoryMapper.toDTO(category));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        Category updatedEntity = CategoryMapper.toEntity(dto);
        Category updatedCategory = categoryService.update(id, updatedEntity);
        userActivityLogger.log("Updated category with ID: " + id);
        return ResponseEntity.ok(CategoryMapper.toDTO(updatedCategory));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        userActivityLogger.log("Deleted category with ID: " + id);
        return ResponseEntity.noContent().build();
    }
}
