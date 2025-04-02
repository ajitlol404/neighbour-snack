package com.neighbour_snack.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.neighbour_snack.entity.Category;
import com.neighbour_snack.entity.Product;
import com.neighbour_snack.entity.Product.UnitType;
import com.neighbour_snack.helper.AppUtil;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
    public record ProductRequestDTO(
            @NotBlank(message = "Product name is required") @Size(min = 3, max = 150, message = "Product name must be between 3 and 150 characters") @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "Category name can only contain letters, numbers, hyphens (-), apostrophes ('), slashes (/), parentheses (()), and ampersands (&).") String name,
            @Size(max = 500, message = "Product description must not exceed 500 characters") String description,
            @Digits(integer = 10, fraction = 2, message = "Price must have up to 10 digits before the decimal and up to 2 digits after the decimal") @Positive(message = "Price must be greater than zero") BigDecimal price,
            @NotNull(message = "Stock status is required") boolean inStock,
            @NotNull(message = "Category ID is required") UUID categoryId,
            @NotNull(message = "Unit type is required") UnitType unitType) {
        public Product toEntity(Category category) {
            return Product.builder()
                    .name(name.toLowerCase())
                    .normalizedName(AppUtil.normalizeName(name))
                    .description(description)
                    .price(price)
                    .inStock(inStock)
                    .category(category)
                    .unitType(unitType)
                    .build();
        }

        public Product applyUpdatesTo(Product product, Category category) { // New method for updates
            product.setName(name.toLowerCase());
            product.setNormalizedName(AppUtil.normalizeName(name));
            product.setDescription(description);
            product.setPrice(price);
            product.setInStock(inStock);
            product.setCategory(category);
            product.setUnitType(unitType);
            return product;
        }
    }

    public record ProductResponseDTO(
            UUID uuid, String name, String description, String normalizedName,
            BigDecimal price, boolean inStock, String productImage,
            String categoryName, UUID categoryUuid, UnitType unitType,
            ZonedDateTime createdAt, ZonedDateTime updatedAt) {

        public static ProductResponseDTO fromEntity(Product product) {
            return new ProductResponseDTO(
                    product.getUuid(),
                    product.getName(),
                    product.getDescription(),
                    product.getNormalizedName(),
                    product.getPrice(),
                    product.isInStock(),
                    product.getProductImage(),
                    product.getCategory().getName(),
                    product.getCategory().getUuid(),
                    product.getUnitType(),
                    product.getCreatedAt(),
                    product.getUpdatedAt());
        }
    }

    public record ProductPublicResponseDTO(
            UUID uuid,
            String name,
            String normalizedName,
            String description,
            BigDecimal price,
            boolean inStock,
            String productImage,
            String categoryName,
            UUID categoryUuid,
            UnitType unitType) {

        public static ProductPublicResponseDTO fromEntity(Product product) {
            return new ProductPublicResponseDTO(
                    product.getUuid(),
                    product.getName(),
                    product.getNormalizedName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.isInStock(),
                    product.getProductImage(),
                    product.getCategory().getName(),
                    product.getCategory().getUuid(),
                    product.getUnitType());
        }
    }
}
