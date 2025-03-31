package com.neighbour_snack.dto;

import java.util.List;

public record PaginationResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast) {
}