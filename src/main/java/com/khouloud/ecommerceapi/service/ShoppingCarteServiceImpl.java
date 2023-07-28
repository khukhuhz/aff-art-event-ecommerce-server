package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Action;
import com.khouloud.ecommerceapi.dto.ProductDtoResponse;
import com.khouloud.ecommerceapi.dto.ShoppingCarteDtoResponse;
import com.khouloud.ecommerceapi.dto.ShoppingCarteItemDtoRequest;
import com.khouloud.ecommerceapi.dto.ShoppingCarteItemDtoResponse;
import com.khouloud.ecommerceapi.exceptions.NoProductExistsException;
import com.khouloud.ecommerceapi.model.Product;
import com.khouloud.ecommerceapi.model.ShoppingCarte;
import com.khouloud.ecommerceapi.model.User;
import com.khouloud.ecommerceapi.repository.ShoppingCarteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShoppingCarteServiceImpl implements ShoppingCarteService {

    public static final String NO_PRODUCT_WITH_ID_IN_SHOPPING_CARTE = "No Product with id: %s in shopping carte";
    public static final String NO_PRODUCT_IN_SHOPPING_CARTE = "No Product in shopping carte";
    public static final String NO_PRODUCT_WITH_ID_FOUND_IN_SHOPPING_CARTE = "No product with id: %s found in shopping carte";

    private AuthenticationService authenticationService;
    private ProductService productService;
    private ShoppingCarteRepository shoppingCarteRepository;
    private ModelMapper modelMapper;

    @Override
    public void addProductToShoppingCart(ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, String token) {
        User user = this.authenticationService.findUserByToken(token);
        Long productId = shoppingCarteItemDtoRequest.getProductId();
        Product product = this.productService.getProductEntity(productId);

        ShoppingCarte shoppingCarte = this.shoppingCarteRepository.findByUserAndProduct(user, product).orElseGet(ShoppingCarte::new);
        if (Objects.nonNull(shoppingCarte.getShoppingCarteId())) {
            int newProductQte = shoppingCarte.getQuantity() + shoppingCarteItemDtoRequest.getQuantity();
            shoppingCarte.setQuantity(newProductQte);
        } else {
            shoppingCarte = buildShoppingCarteEntity(shoppingCarteItemDtoRequest, user, product);
        }
        this.shoppingCarteRepository.save(shoppingCarte);
    }

    @Override
    public ShoppingCarteDtoResponse getShoppingCarteContentByUser(String token) {
        User user = this.authenticationService.findUserByToken(token);

        List<ShoppingCarte> shoppingCarteList = this.shoppingCarteRepository.findByUserOrderByCreatedDateDesc(user);
        List<ShoppingCarteItemDtoResponse> shoppingCarteItemDtoResponses = buildShoppingCarteItemResponse(shoppingCarteList);
        double totalPrice = calculateTotalPriceOfItemsShoppingCarte(shoppingCarteItemDtoResponses);
        ShoppingCarteDtoResponse shoppingCarteDtoResponse = buildShoppingCateDtoResponse(shoppingCarteItemDtoResponses, totalPrice);
        return shoppingCarteDtoResponse;
    }

    @Override
    public void deleteItemFromShoppingCarte(Long productId, String token) {
        User user = this.authenticationService.findUserByToken(token);
        Product product = this.productService.getProductEntity(productId);

        ShoppingCarte shoppingCarte = this.shoppingCarteRepository.findByUserAndProduct(user, product).orElseGet(ShoppingCarte::new);
        if (Objects.isNull(shoppingCarte.getShoppingCarteId())) {
            throw new NoProductExistsException(String.format(NO_PRODUCT_WITH_ID_IN_SHOPPING_CARTE, productId));
        }
        this.shoppingCarteRepository.delete(shoppingCarte);
    }

    @Override
    public void deleteAllItemsFromShoppingCarte(String token) {
        User user = this.authenticationService.findUserByToken(token);
        List<ShoppingCarte> shoppingCarts = this.shoppingCarteRepository.findByUser(user);
        if (Objects.isNull(shoppingCarts) || shoppingCarts.isEmpty()) {
            throw new NoProductExistsException(NO_PRODUCT_IN_SHOPPING_CARTE);
        }
        List<Long> shoppingCarteIds = shoppingCarts.stream().map(ShoppingCarte::getShoppingCarteId).collect(Collectors.toList());
        this.shoppingCarteRepository.deleteAllByIdInBatch(shoppingCarteIds);
    }

    @Override
    public ShoppingCarteItemDtoResponse updateQuantityProductFromShoppingCarte(ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, Action action, String token) {
        User user = this.authenticationService.findUserByToken(token);
        Product product = this.productService.getProductEntity(shoppingCarteItemDtoRequest.getProductId());
        ShoppingCarte shoppingCarte = this.shoppingCarteRepository.findByUserAndProduct(user, product).orElseThrow(()->
                trowNoProductExistsException(shoppingCarteItemDtoRequest, user, product));

        int newProductQte = calculateNewQuantity(shoppingCarteItemDtoRequest.getQuantity(), action, shoppingCarte);

        shoppingCarte.setQuantity(newProductQte);
        ShoppingCarte savedShoppingCarte = this.shoppingCarteRepository.save(shoppingCarte);
        ShoppingCarteItemDtoResponse shoppingCarteItemDtoResponse = mapShoppingCarteEntityToItemDtoResponse(savedShoppingCarte);
        return shoppingCarteItemDtoResponse;
    }

    private int calculateNewQuantity(Integer quantity, Action action, ShoppingCarte shoppingCarte) {
        return action == Action.MINUS ? Integer.max(shoppingCarte.getQuantity() - quantity, 0) :
                shoppingCarte.getQuantity() + quantity;
    }

    private ShoppingCarteDtoResponse buildShoppingCateDtoResponse(List<ShoppingCarteItemDtoResponse> shoppingCarteItemDtoResponses, double totalPrice) {
        return ShoppingCarteDtoResponse.builder()//
                .shoppingCarteItemDtoResponses(shoppingCarteItemDtoResponses)//
                .totalPrice(totalPrice)//
                .totalNumberOfItems(shoppingCarteItemDtoResponses.size())//
                .build();
    }

    private double calculateTotalPriceOfItemsShoppingCarte(List<ShoppingCarteItemDtoResponse> shoppingCarteItemDtoResponses) {
        return shoppingCarteItemDtoResponses.stream()//
                .mapToDouble(dto -> dto.getQuantity() * dto.getProductDtoResponse().getPrice())//
                .sum();
    }

    private ShoppingCarte buildShoppingCarteEntity(ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, User user, Product product) {
        return ShoppingCarte.builder()//
                .createdDate(LocalDateTime.now())//
                .user(user)//
                .product(product)//
                .quantity(shoppingCarteItemDtoRequest.getQuantity())//
                .build();
    }

    private ShoppingCarteItemDtoResponse mapShoppingCarteEntityToItemDtoResponse(ShoppingCarte shoppingCarte) {
        ShoppingCarteItemDtoResponse shoppingCarteItemDtoResponse = this.modelMapper.map(shoppingCarte, ShoppingCarteItemDtoResponse.class);
        shoppingCarteItemDtoResponse.setShoppingCarteItemId(shoppingCarte.getShoppingCarteId());
        ProductDtoResponse productDtoResponse = this.modelMapper.map(shoppingCarte.getProduct(), ProductDtoResponse.class);
        shoppingCarteItemDtoResponse.setProductDtoResponse(productDtoResponse);
        return shoppingCarteItemDtoResponse;
    }

    private List<ShoppingCarteItemDtoResponse> buildShoppingCarteItemResponse(List<ShoppingCarte> shoppingCarteList) {
        return shoppingCarteList.stream()//
                .map(this::mapShoppingCarteEntityToItemDtoResponse)//
                .collect(Collectors.toList());
    }

    private NoProductExistsException trowNoProductExistsException(ShoppingCarteItemDtoRequest shoppingCarteItemDtoRequest, User user, Product product) {
        log.error("Data sent to change product quantity : User {}, Product: {}", user, product);
        log.error("No product with id {} found in shopping carte", shoppingCarteItemDtoRequest.getProductId());
        return new NoProductExistsException(String.format(NO_PRODUCT_WITH_ID_FOUND_IN_SHOPPING_CARTE, shoppingCarteItemDtoRequest.getProductId()));
    }
}
