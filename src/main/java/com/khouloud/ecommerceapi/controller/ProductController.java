package com.khouloud.ecommerceapi.controller;

import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.dto.ProductDtoRequest;
import com.khouloud.ecommerceapi.dto.ProductDtoResponse;
import com.khouloud.ecommerceapi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/e-commerce-api/v1/products")
public class ProductController {

    public static final String NO_PRODUCT_EXIST = "Aucun produit n'existe";
    public static final String PRODUCT_WITH_ID_WAS_DELETED = "Produit avec id %s a été supprimé";
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<ProductDtoResponse> createProduct(@Valid @RequestBody ProductDtoRequest productDtoRequest, @RequestParam("token") String token){
       ProductDtoResponse productDtoResponse = this.productService.createProduct(productDtoRequest, token);
        return new ResponseEntity<>(productDtoResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> getAllProduct(){
        List<ProductDtoResponse> productDtoResponses = this.productService.getAllProduct();
        if(productDtoResponses.isEmpty()){
            return new ResponseEntity<>(new EcommerceApiResponse(false, NO_PRODUCT_EXIST), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDtoResponses, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<ProductDtoResponse> getProductById(@PathVariable("productId") Long productId){
        ProductDtoResponse productDtoResponse = this.productService.findProductById(productId);
        return new ResponseEntity<>(productDtoResponse, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<ProductDtoResponse> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductDtoRequest productDtoRequest, @RequestParam("token") String token){
        ProductDtoResponse productDtoResponse = this.productService.updateProduct(productId, productDtoRequest, token);
        return new ResponseEntity<>(productDtoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<EcommerceApiResponse> deleteProduct(@PathVariable("productId") Long productId, @RequestParam("token") String token){
        boolean isProductDeleted = this.productService.deleteProduct(productId, token);
        String message = String.format(PRODUCT_WITH_ID_WAS_DELETED, productId);
        return new ResponseEntity<>(new EcommerceApiResponse(isProductDeleted, message), HttpStatus.OK);
    }
}
