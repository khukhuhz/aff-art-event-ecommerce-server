package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.ProductDtoRequest;
import com.khouloud.ecommerceapi.dto.ProductDtoResponse;
import com.khouloud.ecommerceapi.model.Product;

import java.util.List;

public interface ProductService {
    ProductDtoResponse createProduct(ProductDtoRequest productDtoRequest, String token);

    List<ProductDtoResponse> getAllProduct();

    ProductDtoResponse findProductById(Long productId);

    ProductDtoResponse updateProduct(Long productId, ProductDtoRequest productDtoRequest, String token);

    Product getProductEntity(Long productId);

    boolean deleteProduct(Long productId, String token);
}
