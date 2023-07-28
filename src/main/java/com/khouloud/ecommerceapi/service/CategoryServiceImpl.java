package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.CategoryDtoRequest;
import com.khouloud.ecommerceapi.dto.CategoryDtoResponse;
import com.khouloud.ecommerceapi.exceptions.NoCategoryExistsException;
import com.khouloud.ecommerceapi.model.Category;
import com.khouloud.ecommerceapi.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    public static final String NO_CATEGORY_EXIST_WITH_ID = "Aucune catégorie n'existe avec id: %s";
    private CategoryRepository categoryRepository;
    private AuthenticationService authenticationService;
    private ModelMapper modelMapper;


    @Override
    public CategoryDtoResponse createCategory(CategoryDtoRequest categoryDtoRequest, String token) {
        this.authenticationService.verifyIfUserIsAdmin(token);
        Category category = this.modelMapper.map(categoryDtoRequest, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDtoResponse categoryDtoResponse = this.modelMapper.map(savedCategory, CategoryDtoResponse.class);
        return categoryDtoResponse;
    }

    @Override
    public List<CategoryDtoResponse> getAllCategory() {
        List<CategoryDtoResponse> categoryDtoResponses = categoryRepository.findAll().stream()//
                .map(cat -> this.modelMapper.map(cat, CategoryDtoResponse.class))//
                .collect(Collectors.toList());
        return categoryDtoResponses;
    }

    @Override
    public CategoryDtoResponse updateCategory(Integer categoryId, CategoryDtoRequest categoryDtoRequest, String token) {
        this.authenticationService.verifyIfUserIsAdmin(token);
        this.findCategoryById(categoryId);
        Category categoryToUpdate = this.modelMapper.map(categoryDtoRequest, Category.class);
        categoryToUpdate.setCategoryId(categoryId);
        Category updatedCategory = this.categoryRepository.save(categoryToUpdate);
        CategoryDtoResponse updatedCategoryDtoResponse = this.modelMapper.map(updatedCategory, CategoryDtoResponse.class);
        return updatedCategoryDtoResponse;
    }

    @Override
    public CategoryDtoResponse findCategoryById(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseGet(() -> new Category());
        if(Objects.isNull(category.getCategoryId())){
            throw new NoCategoryExistsException(String.format(NO_CATEGORY_EXIST_WITH_ID, categoryId));
        }
        CategoryDtoResponse categoryDtoResponse = this.modelMapper.map(category, CategoryDtoResponse.class);
        return categoryDtoResponse;
    }
}
