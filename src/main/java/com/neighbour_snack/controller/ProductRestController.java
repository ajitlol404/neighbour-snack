package com.neighbour_snack.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.neighbour_snack.dto.ProductDTO.ProductRequestDTO;
import com.neighbour_snack.dto.ProductDTO.ProductResponseDTO;
import com.neighbour_snack.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        return productService.createProduct(productRequest);
    }

    @PatchMapping("/{pid}/images")
    public ProductResponseDTO uploadProductImage(@PathVariable UUID pid, @RequestParam MultipartFile image) {
        return productService.uploadProductImage(pid, image);
    }

    @GetMapping("/{pid}/images")
    public ResponseEntity<Resource> getMethodName(@PathVariable UUID pid) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(productService.getProductImageByProductUuid(pid));
    }

    @GetMapping("/{uuid}")
    public ProductResponseDTO getProductByUuid(@PathVariable UUID uuid) {
        return productService.getProductByUuid(uuid);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCategoryByUuid(@PathVariable UUID uuid) {
        productService.deleteProductByUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{uuid}")
    public ProductResponseDTO updateProduct(@PathVariable UUID uuid,
            @Valid @RequestBody ProductRequestDTO productRequest) {
        return productService.updateProduct(uuid, productRequest);
    }

}
