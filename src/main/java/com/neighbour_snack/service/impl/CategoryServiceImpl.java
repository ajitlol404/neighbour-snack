package com.neighbour_snack.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.neighbour_snack.dao.CategoryRepository;
import com.neighbour_snack.dto.CategoryDTO;
import com.neighbour_snack.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighbour_snack.dto.CategoryDTO.CategoryRequestDTO;
import com.neighbour_snack.dto.CategoryDTO.CategoryResponseDTO;
import com.neighbour_snack.entity.Category;
import com.neighbour_snack.service.CategoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO createCategoryDTO) {

        // Check if category already exists
        if (categoryRepository.existsByName(createCategoryDTO.name().toLowerCase())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        // Convert DTO to entity, set normalizedName, and save
        Category category = createCategoryDTO.toEntity();
        Category savedCategory = categoryRepository.save(category);

        return CategoryDTO.CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryByUuid(UUID uuid) {
        Category category = categoryRepository.findCategoryByUuid(uuid);
        return CategoryDTO.CategoryResponseDTO.fromEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findCategoryByUuid(uuid);

        // Check if the name is being updated
        if (!existingCategory.getName().equalsIgnoreCase(categoryRequestDTO.name()) &&
                categoryRepository.existsByName(categoryRequestDTO.name().toLowerCase())) {
            throw new IllegalArgumentException("Category with this name already exists");
        }

        // Update and save the category in one step
        Category savedCategory = categoryRepository.save(categoryRequestDTO.updateCategory(existingCategory));

        return CategoryDTO.CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryByUuid(UUID uuid) {
        Category category = categoryRepository.findCategoryByUuid(uuid);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryPublicResponseDTO> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(CategoryDTO.CategoryPublicResponseDTO::fromEntity)
                .toList();
    }

}
