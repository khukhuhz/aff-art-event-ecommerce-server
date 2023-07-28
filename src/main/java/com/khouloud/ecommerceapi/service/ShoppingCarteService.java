package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Action;
import com.khouloud.ecommerceapi.dto.ShoppingCarteDtoResponse;
import com.khouloud.ecommerceapi.dto.ShoppingCarteItemDtoRequest;
import com.khouloud.ecommerceapi.dto.ShoppingCarteItemDtoResponse;

public interface ShoppingCarteService {
    void addProductToShoppingCart(ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, String token);

    ShoppingCarteDtoResponse getShoppingCarteContentByUser(String token);

    void deleteItemFromShoppingCarte(Long productId, String token);

    void deleteAllItemsFromShoppingCarte(String token);

    ShoppingCarteItemDtoResponse updateQuantityProductFromShoppingCarte(ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, Action action, String token);
}
