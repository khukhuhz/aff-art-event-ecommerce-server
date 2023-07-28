package com.khouloud.ecommerceapi.controller;

import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.dto.ProductDtoResponse;
import com.khouloud.ecommerceapi.dto.WishListDtoRequest;
import com.khouloud.ecommerceapi.service.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/e-commerce-api/v1/wishlists")
public class WishListController {

    public static final String PRODUCT_WITH_ID_WAS_ADDED_TO_WISHLIST = "Produit avec id: %s a été ajouté à la liste de souhaits";
    public static final String PRODUCT_WITH_ID_WAS_DELETED = "Produit avec id: %s a été supprimé de la liste de souhaits";
    public static final String ALL_PRODUCT_WERE_DELETED = "Tous les produits ont été supprimés de la liste de souhaits";
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<EcommerceApiResponse> addToWishList(@Valid @RequestBody WishListDtoRequest wishListDtoRequest, @RequestParam("token") String token) {
        Long productId = wishListDtoRequest.getProductId();
        this.wishListService.createWishList(productId, token);
        EcommerceApiResponse ecommerceApiResponse = new EcommerceApiResponse(true, String.format(PRODUCT_WITH_ID_WAS_ADDED_TO_WISHLIST, productId));
        return new ResponseEntity<>(ecommerceApiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ProductDtoResponse>> listWishListContent(@RequestParam("token") String token) {
        List<ProductDtoResponse> productDtoResponses = this.wishListService.getAllProductInWishList(token);
        return new ResponseEntity<>(productDtoResponses, HttpStatus.OK);
    }

    @DeleteMapping("/products/{productId}")
    @ResponseBody
    public ResponseEntity<EcommerceApiResponse> deleteOneProductFromWishList(@RequestParam("token") String token, @PathVariable("productId") Long productId) {

        this.wishListService.deleteOneProductFromWishList(token, productId);
        String message = String.format(PRODUCT_WITH_ID_WAS_DELETED, productId);
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        return new ResponseEntity<>(new EcommerceApiResponse(true, message), httpStatus);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<EcommerceApiResponse> deleteAllProductsFromWishList(@RequestParam("token") String token) {

        this.wishListService.deleteAllProductFromWishList(token);
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        return new ResponseEntity<>(new EcommerceApiResponse(true, ALL_PRODUCT_WERE_DELETED), httpStatus);
    }
}
