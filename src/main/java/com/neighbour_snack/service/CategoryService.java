package com.neighbour_snack.service;

import java.util.List;
import java.util.UUID;

import com.neighbour_snack.dto.CategoryDTO;
import com.neighbour_snack.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighbour_snack.dto.CategoryDTO.CategoryResponseDTO;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryByUuid(UUID uuid);

    CategoryResponseDTO updateCategoryByUuid(UUID uuid, CategoryDTO.CategoryRequestDTO categoryRequestDTO);

    void deleteCategoryByUuid(UUID uuid);

    List<CategoryPublicResponseDTO> getActiveCategories();

}
