package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.ProductDtoResponse;
import com.khouloud.ecommerceapi.exceptions.NoProductExistsException;
import com.khouloud.ecommerceapi.exceptions.ProductAlreadyExistException;
import com.khouloud.ecommerceapi.model.Product;
import com.khouloud.ecommerceapi.model.User;
import com.khouloud.ecommerceapi.model.WishList;
import com.khouloud.ecommerceapi.repository.WishListRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class WishListServiceImpl implements WishListService {

    public static final String PRODUCT_WITH_ID_ALREADY_EXIST_IN_WISH_LIST = "Produit avec id: %s existe déjà dans la liste de souhaits";
    public static final String NO_PRODUCT_WITH_ID_WISH_LIST = "Aucun produit avec id: %s WishList";
    public static final String NO_PRODUCTS_IN_WISH_LIST = "Aucun produit dans la liste de souhaits";
    private WishListRepository wishListRepository;
    private AuthenticationService authenticationService;
    private ModelMapper modelMapper;
    private ProductService productService;

    @Override
    public void createWishList(Long productId, String token) {
        User user = this.authenticationService.getUser(token);
        Product product = this.productService.getProductEntity(productId);

        ProductDtoResponse productInWishList = this.getProductInWishListOfUser(productId, token);
        if (Objects.nonNull(productInWishList)) {
            throw new ProductAlreadyExistException(String.format(PRODUCT_WITH_ID_ALREADY_EXIST_IN_WISH_LIST, productId));
        }

        WishList wishList = WishList.builder()
                .product(product)
                .user(user)
                .createdDate(LocalDateTime.now())
                .build();
        this.wishListRepository.save(wishList);
    }

    @Override
    public List<ProductDtoResponse> getAllProductInWishList(String token) {
        User user = this.authenticationService.getUser(token);
        List<WishList> wishLists = this.wishListRepository.findByUserOrderByCreatedDateDesc(user);
        List<ProductDtoResponse> productDtoResponses = wishLists.stream()
                .map(wishList -> this.modelMapper.map(wishList.getProduct(), ProductDtoResponse.class))
                .collect(Collectors.toList());
        return productDtoResponses;
    }

    @Override
    public ProductDtoResponse getProductInWishListOfUser(Long productId, String token) {
        User user = this.authenticationService.getUser(token);
        Product product = this.productService.getProductEntity(productId);

        WishList wishList = this.wishListRepository.findByUserAndProduct(user, product);
        if (Objects.isNull(wishList)) {
            return null;
        }
        Product foundProduct = wishList.getProduct();
        ProductDtoResponse productDtoResponse = this.modelMapper.map(foundProduct, ProductDtoResponse.class);
        return productDtoResponse;
    }

    @Override
    public void deleteOneProductFromWishList(String token, Long productId) {
        User user = this.authenticationService.getUser(token);
        Product product = this.productService.getProductEntity(productId);

        WishList wishList = this.wishListRepository.findByUserAndProduct(user, product);
        if (Objects.isNull(wishList)) {
            throw new NoProductExistsException(String.format(NO_PRODUCT_WITH_ID_WISH_LIST, productId));

        }
        this.wishListRepository.delete(wishList);
    }

    @Override
    public void deleteAllProductFromWishList(String token) {
        User user = this.authenticationService.getUser(token);
        List<Long> wishListIds = this.wishListRepository.findByUserOrderByCreatedDateDesc(user)//
                .stream().map(wishList -> wishList.getWishListId())//
                .collect(Collectors.toList());
        if (wishListIds.isEmpty()) {
            throw new NoProductExistsException(NO_PRODUCTS_IN_WISH_LIST);
        }
        this.wishListRepository.deleteAllByIdInBatch(wishListIds);
    }

}
