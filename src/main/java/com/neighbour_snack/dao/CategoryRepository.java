package com.neighbour_snack.dao;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neighbour_snack.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByName(String name);

    boolean existsByNormalizedName(String normalizedName);

    Optional<Category> findByName(String name);

    Optional<Category> findByUuid(UUID uuid);

    default Category findCategoryByUuid(UUID uuid) {
        return findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Category with [UUID= " + uuid + "] not found"));
    }

    Optional<Category> findByNormalizedName(String normalizedName);

    default Category findCategoryByNormalizedName(String normalizedName) {
        return findByNormalizedName(normalizedName)
                .orElseThrow(() -> new NoSuchElementException(
                        "Category with [normalizedName= " + normalizedName + "] not found"));
    }

    List<Category> findByIsActiveTrue();

}
