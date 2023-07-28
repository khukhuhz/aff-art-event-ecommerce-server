package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.CategoryDtoRequest;
import com.khouloud.ecommerceapi.dto.CategoryDtoResponse;
import com.khouloud.ecommerceapi.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDtoResponse createCategory(CategoryDtoRequest categoryDtoRequest, String token);

    List<CategoryDtoResponse> getAllCategory();
    
    CategoryDtoResponse updateCategory(Integer categoryId, CategoryDtoRequest categoryDtoRequest, String token);

    CategoryDtoResponse findCategoryById(Integer categoryId);
}
