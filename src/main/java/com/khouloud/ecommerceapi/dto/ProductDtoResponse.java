package com.khouloud.ecommerceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDtoResponse {

    @NotBlank
    private Long productId;

    @NotBlank
    private String productName;

    private String image;

    @NotNull
    private Double price;

    @NotBlank
    private String description;

    @NotBlank
    private Integer categoryId;

    private String categoryName;
}
