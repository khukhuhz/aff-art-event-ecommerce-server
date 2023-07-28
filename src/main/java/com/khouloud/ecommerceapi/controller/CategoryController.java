package com.khouloud.ecommerceapi.controller;

import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.dto.CategoryDtoRequest;
import com.khouloud.ecommerceapi.dto.CategoryDtoResponse;
import com.khouloud.ecommerceapi.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/e-commerce-api/v1/categories")
public class CategoryController {
    public static final String NO_CATEGORY_EXIST = "Aucune catégorie n'existe";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<CategoryDtoResponse> createCategory(@Valid @RequestBody CategoryDtoRequest categoryDtoRequest, @RequestParam("token") String token){
        CategoryDtoResponse categoryDtoResponse = categoryService.createCategory(categoryDtoRequest, token);
        return new ResponseEntity<>(categoryDtoResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> listAllCategory(){
        List<CategoryDtoResponse> categoryDtoResponses = categoryService.getAllCategory();
        if(categoryDtoResponses.isEmpty()){
            return new ResponseEntity<>(new EcommerceApiResponse(false, NO_CATEGORY_EXIST), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryDtoResponses, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    @ResponseBody
    public ResponseEntity<CategoryDtoResponse> updateCategory(@PathVariable("categoryId") Integer categoryId, @Valid @RequestBody CategoryDtoRequest categoryDtoRequest, @RequestParam("token") String token){
        CategoryDtoResponse categoryDtoResponse = categoryService.updateCategory(categoryId, categoryDtoRequest, token);
        return new ResponseEntity<>(categoryDtoResponse, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @ResponseBody
    public ResponseEntity<CategoryDtoResponse> getCategoryById(@PathVariable("categoryId") Integer categoryId){
        CategoryDtoResponse categoryDtoResponse = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(categoryDtoResponse, HttpStatus.OK);
    }
}
