package com.code81.library.Service;

import com.code81.library.Entity.Category;
import com.code81.library.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllRootCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

    @Transactional
    public Category create(Category category, Long parentId) {
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parent);
            parent.getSubCategories().add(category);
        }
        return categoryRepository.save(category);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Transactional
    public Category update(Long id, Category updatedCategory) {
        Category category = getById(id);
        category.setName(updatedCategory.getName());
        // Update parent if needed (optional: add logic here)
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
