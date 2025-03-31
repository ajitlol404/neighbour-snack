package com.neighbour_snack.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighbour_snack.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsByName(String name);

    boolean existsByNormalizedName(String normalizedName);

    Optional<Product> findByUuid(UUID uuid);

    default Product findProductByUuid(UUID uuid) {
        return findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Product with [UUID= " + uuid + "] not found"));
    }

    Optional<Product> findByNormalizedName(String normalizedName);

    default Product findProductByNormalizedName(String normalizedName) {
        return findByNormalizedName(normalizedName)
                .orElseThrow(() -> new NoSuchElementException(
                        "Product with [normalizedName= " + normalizedName + "] not found"));
    }

}
