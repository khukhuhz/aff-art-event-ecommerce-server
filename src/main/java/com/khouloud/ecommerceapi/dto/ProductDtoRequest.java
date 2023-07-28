package com.khouloud.ecommerceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoRequest {

    @NotBlank
    @Length(min = 3, max = 25)
    private String productName;

    private String image;
    @NotNull
    @DecimalMin(value = "0.0")
    private Double price;

    @NotBlank
    private String description;

    @NotNull
    private Integer categoryId;
}
