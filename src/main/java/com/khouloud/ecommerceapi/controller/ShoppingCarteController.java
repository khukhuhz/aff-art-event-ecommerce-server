package com.khouloud.ecommerceapi.controller;

import com.khouloud.ecommerceapi.common.Action;
import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.dto.ShoppingCarteDtoResponse;
import com.khouloud.ecommerceapi.dto.ShoppingCarteItemDtoRequest;
import com.khouloud.ecommerceapi.dto.ShoppingCarteItemDtoResponse;
import com.khouloud.ecommerceapi.service.ShoppingCarteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/e-commerce-api/v1/shoppingcartes")
@AllArgsConstructor
public class ShoppingCarteController {

    public static final String PRODUCT_WITH_ID_WAS_ADDED_TO_SHOPPING_CARTE = "Produit avec id: %s a été ajouté à la carte d'achat";
    public static final String PRODUCT_WITH_ID_WAS_DELETED_FROM_SHOPPING_CARTE = "Produit avec id: %s a été supprimé de la carte d'achat";
    public static final String ALL_PRODUCTS_WAS_DELETED_FROM_SHOPPING_CARTE = "Tous les produits ont été supprimés de la carte d'achat";
    private ShoppingCarteService shoppingCarteService;

    @PostMapping
    public ResponseEntity<EcommerceApiResponse> addProductToShoppingCate(@Valid @RequestBody ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, @RequestParam("token") String token) {
        this.shoppingCarteService.addProductToShoppingCart(shoppingCarteItemDtoRequest, token);
        String message = String.format(PRODUCT_WITH_ID_WAS_ADDED_TO_SHOPPING_CARTE, shoppingCarteItemDtoRequest.getProductId());
        return new ResponseEntity<>(new EcommerceApiResponse(true, message), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ShoppingCarteDtoResponse> listShoppingCarteContent(@RequestParam("token") String token) {
        ShoppingCarteDtoResponse shoppingCarteDtoResponse = this.shoppingCarteService.getShoppingCarteContentByUser(token);
        return new ResponseEntity<>(shoppingCarteDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<EcommerceApiResponse> deleteItemFromShoppingCarte(@PathVariable("productId") Long productId, @RequestParam("token") String token) {
        this.shoppingCarteService.deleteItemFromShoppingCarte(productId, token);
        String message = String.format(PRODUCT_WITH_ID_WAS_DELETED_FROM_SHOPPING_CARTE, productId);
        return new ResponseEntity<>(new EcommerceApiResponse(true, message), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<EcommerceApiResponse> deleteAllItemsFromShoppingCarte(@RequestParam("token") String token) {
        this.shoppingCarteService.deleteAllItemsFromShoppingCarte(token);
        return new ResponseEntity<>(new EcommerceApiResponse(true, ALL_PRODUCTS_WAS_DELETED_FROM_SHOPPING_CARTE), HttpStatus.ACCEPTED);
    }

    @PatchMapping
    public ResponseEntity<ShoppingCarteItemDtoResponse> updateProductQuantityInShoppingCarte(@Valid @RequestBody ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest,
                                                                                             @RequestParam("action") Action action, @RequestParam("token") String token) {

        ShoppingCarteItemDtoResponse shoppingCarteItemDtoResponse = this.shoppingCarteService.updateQuantityProductFromShoppingCarte(shoppingCarteItemDtoRequest, action, token);
        return new ResponseEntity<>(shoppingCarteItemDtoResponse, HttpStatus.OK);
    }

}
