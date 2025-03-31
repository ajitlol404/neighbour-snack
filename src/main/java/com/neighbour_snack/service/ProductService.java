package com.neighbour_snack.service;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.neighbour_snack.dto.ProductDTO.ProductPublicResponseDTO;
import com.neighbour_snack.dto.ProductDTO.ProductRequestDTO;
import com.neighbour_snack.dto.ProductDTO.ProductResponseDTO;

public interface ProductService {

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO createProduct(ProductRequestDTO productRequest);

    ProductResponseDTO uploadProductImage(UUID pid, MultipartFile file);

    Resource getProductImageByProductUuid(UUID pid);

    ProductResponseDTO getProductByUuid(UUID uuid);

    void deleteProductByUuid(UUID uuid);

    ProductResponseDTO updateProduct(UUID uuid, ProductRequestDTO productRequest);

    List<ProductPublicResponseDTO> getAllPublicProducts();
}
