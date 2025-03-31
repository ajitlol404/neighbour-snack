package com.neighbour_snack.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.neighbour_snack.dto.CategoryDTO.CategoryPublicResponseDTO;
import com.neighbour_snack.dto.ProductDTO.ProductPublicResponseDTO;
import com.neighbour_snack.service.CategoryService;
import com.neighbour_snack.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DashboardRestController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/categories")
    public List<CategoryPublicResponseDTO> getAllCategories() {
        return categoryService.getActiveCategories();
    }

    @GetMapping("/products")
    public List<ProductPublicResponseDTO> getAllProducts() {
        return productService.getAllPublicProducts();
    }

    @GetMapping("/products/{pid}/images")
    public ResponseEntity<Resource> getMethodName(@PathVariable UUID pid) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(productService.getProductImageByProductUuid(pid));
    }
}
