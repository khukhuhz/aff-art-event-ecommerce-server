package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.ProductDtoRequest;
import com.khouloud.ecommerceapi.dto.ProductDtoResponse;
import com.khouloud.ecommerceapi.exceptions.NoCategoryExistsException;
import com.khouloud.ecommerceapi.exceptions.NoProductExistsException;
import com.khouloud.ecommerceapi.model.Category;
import com.khouloud.ecommerceapi.model.Product;
import com.khouloud.ecommerceapi.repository.CategoryRepository;
import com.khouloud.ecommerceapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final String NO_PRODUCT_WITH_ID_EXIST = "Aucun produit avec id: %s existe";
    public static final String NO_CATEGORY_WITH_ID_EXIST = "Aucune catégorie avec id: %s existe";
    public static final String NO_PRODUCT_FOUND_WITH_ID = "Aucun produit trouvé avec id: %s";

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private AuthenticationService authenticationService;

    @Override
    public ProductDtoResponse createProduct(ProductDtoRequest productDtoRequest, String token) {
        this.authenticationService.verifyIfUserIsAdmin(token);
        Category category = this.categoryRepository.findById(productDtoRequest.getCategoryId()).orElseGet(Category::new);
        if (Objects.isNull(category.getCategoryId())) {
            throw new NoCategoryExistsException(String.format(NO_CATEGORY_WITH_ID_EXIST, productDtoRequest.getCategoryId()));
        }

        Product productToSave = this.modelMapper.map(productDtoRequest, Product.class);
        productToSave.setCategory(category);
        Product savedProduct = this.productRepository.save(productToSave);
        ProductDtoResponse productDtoResponse = this.mapProductToDtoResponse(savedProduct);
        return productDtoResponse;
    }

    @Override
    public List<ProductDtoResponse> getAllProduct() {
        List<ProductDtoResponse> productDtoResponses = this.productRepository.findAll()//
                .stream()//
                .map(prod -> mapProductToDtoResponse(prod))//
                .collect(Collectors.toList());
        return productDtoResponses;
    }

    @Override
    public ProductDtoResponse findProductById(Long productId) {
        Product product = this.productRepository.findById(productId).orElseGet(Product::new);
        if (Objects.isNull(product.getProductId())) {
            throw new NoProductExistsException(String.format(NO_PRODUCT_WITH_ID_EXIST, productId));
        }
        ProductDtoResponse productDtoResponse = this.mapProductToDtoResponse(product);
        return productDtoResponse;
    }

    @Override
    public ProductDtoResponse updateProduct(Long productId, ProductDtoRequest productDtoRequest, String token) {
        this.authenticationService.verifyIfUserIsAdmin(token);
        Integer categoryId = productDtoRequest.getCategoryId();
        Category category = this.categoryRepository.findById(categoryId).orElseGet(Category::new);
        if (Objects.isNull(category.getCategoryId())) {
            throw new NoCategoryExistsException(String.format(NO_CATEGORY_WITH_ID_EXIST, categoryId));
        }

        this.findProductById(productId);

        Product productToUpdate = this.modelMapper.map(productDtoRequest, Product.class);
        productToUpdate.setProductId(productId);
        productToUpdate.setCategory(category);
        Product updatedProduct = this.productRepository.save(productToUpdate);
        ProductDtoResponse productDtoResponse = this.mapProductToDtoResponse(updatedProduct);
        return productDtoResponse;
    }

    @Override
    public Product getProductEntity(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> {
            log.error("No product found with id {}", productId);
            return new NoProductExistsException(String.format(NO_PRODUCT_FOUND_WITH_ID, productId));
        });
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long productId, String token) {
        this.authenticationService.verifyIfUserIsAdmin(token);
        Product product = this.getProductEntity(productId);
        this.productRepository.delete(product);
        return true;
    }

    private ProductDtoResponse mapProductToDtoResponse(Product product) {
        ProductDtoResponse productDtoResponse = this.modelMapper.map(product, ProductDtoResponse.class);
        productDtoResponse.setCategoryId(product.getCategory().getCategoryId());
        productDtoResponse.setCategoryName(product.getCategory().getCategoryName());
        return productDtoResponse;
    }
}

