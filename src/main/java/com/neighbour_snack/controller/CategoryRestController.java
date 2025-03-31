package com.neighbour_snack.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.dto.CategoryDTO;
import com.neighbour_snack.dto.CategoryDTO.CategoryResponseDTO;
import com.neighbour_snack.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryResponseDTO createCategory(@Valid @RequestBody CategoryDTO.CategoryRequestDTO createCategoryDTO) {
        return categoryService.createCategory(createCategoryDTO);
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{uuid}")
    public CategoryResponseDTO getCategoryByUuid(@PathVariable UUID uuid) {
        return categoryService.getCategoryByUuid(uuid);
    }

    @PutMapping("/{uuid}")
    public CategoryResponseDTO updateCategoryByUuid(@PathVariable UUID uuid,
            @Valid @RequestBody CategoryDTO.CategoryRequestDTO categoryRequestDTO) {
        return categoryService.updateCategoryByUuid(uuid, categoryRequestDTO);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCategoryByUuid(@PathVariable UUID uuid) {
        categoryService.deleteCategoryByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

}
