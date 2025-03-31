package com.neighbour_snack.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.neighbour_snack.dao.CategoryRepository;
import com.neighbour_snack.dao.ProductRepository;
import com.neighbour_snack.dto.ProductDTO;
import com.neighbour_snack.dto.ProductDTO.ProductPublicResponseDTO;
import com.neighbour_snack.dto.ProductDTO.ProductRequestDTO;
import com.neighbour_snack.dto.ProductDTO.ProductResponseDTO;
import com.neighbour_snack.entity.Category;
import com.neighbour_snack.entity.Product;
import com.neighbour_snack.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final Path IMAGE_DIRECTORY = Paths.get("products/");

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        Category category = categoryRepository.findCategoryByUuid(productRequest.categoryId());

        // Check if product already exists
        if (productRepository.existsByName(productRequest.name().toLowerCase())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }

        Product savedProduct = productRepository.save(productRequest.toEntity(category));
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    @Override
    public ProductResponseDTO uploadProductImage(UUID pid, MultipartFile file) {

        // Validate if image is present
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }

        // Validate file size (Max: 2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size must not exceed 2MB");
        }

        // Validate file type (only allow JPEG, PNG)
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Only JPEG and PNG images are allowed");
        }

        // Extract file extension dynamically
        String originalFileName = file.getOriginalFilename();
        String fileExtension = (originalFileName != null && originalFileName.contains("."))
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ".jpg"; // Default to .jpg if no extension is found

        Product product = productRepository.findProductByUuid(pid);

        // Generate new file name using product's normalized name
        String normalizedFileName = product.getNormalizedName() + fileExtension;

        try {
            Files.createDirectories(IMAGE_DIRECTORY);
            Path filePath = IMAGE_DIRECTORY.resolve(normalizedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            product.setProductImage(normalizedFileName);
            return ProductResponseDTO.fromEntity(productRepository.save(product));

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image", e);
        }

    }

    @Override
    public Resource getProductImageByProductUuid(UUID pid) {
        Product product = productRepository.findProductByUuid(pid);
        Path imagePath = IMAGE_DIRECTORY.resolve(product.getProductImage());

        try {
            if (!Files.exists(imagePath)) {
                throw new NoSuchElementException("Image not found: " + product.getProductImage());
            }
            return new UrlResource(imagePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Invalid file path for image: " + product.getProductImage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteProductByUuid(UUID uuid) {
        Product product = productRepository.findProductByUuid(uuid);
        if (product.getProductImage() != null) {
            Path imagePath = IMAGE_DIRECTORY.resolve(product.getProductImage());
            try {
                Files.deleteIfExists(imagePath); // Deletes image if it exists
            } catch (IOException e) {
                throw new RuntimeException("Error deleting product image: " + product.getProductImage(), e);
            }
        }

        // Delete the product from the database
        productRepository.delete(product);
    }

    @Override
    public ProductResponseDTO getProductByUuid(UUID uuid) {
        Product product = productRepository.findProductByUuid(uuid);
        return ProductDTO.ProductResponseDTO.fromEntity(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(UUID uuid, ProductRequestDTO productRequest) {
        // Find existing product
        Product existingProduct = productRepository.findProductByUuid(uuid);
        // Check if the new name already exists (excluding the current product)
        if (!existingProduct.getName().equalsIgnoreCase(productRequest.name()) &&
                productRepository.existsByName(productRequest.name().toLowerCase())) {
            throw new IllegalArgumentException("Product with this name already exists");
        }

        // Find category
        Category category = categoryRepository.findCategoryByUuid(productRequest.categoryId());

        // Apply updates using DTO method
        existingProduct = productRequest.applyUpdatesTo(existingProduct, category);

        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Return response DTO
        return ProductResponseDTO.fromEntity(updatedProduct);
    }

    @Override
    public List<ProductPublicResponseDTO> getAllPublicProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductPublicResponseDTO::fromEntity)
                .toList();
    }

}
