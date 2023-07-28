package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.ProductDtoResponse;

import java.util.List;

public interface WishListService {

    void createWishList(Long productId, String token);
    List<ProductDtoResponse> getAllProductInWishList(String token);
    ProductDtoResponse getProductInWishListOfUser(Long productId, String token);
    void deleteOneProductFromWishList(String token, Long productId);
    void deleteAllProductFromWishList(String token);
}
