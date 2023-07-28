package com.khouloud.ecommerceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDtoRequest {

    @NotBlank
    private String categoryName;

    @NotBlank
    private String description;

    @NotBlank
    private String imageURL;
}
