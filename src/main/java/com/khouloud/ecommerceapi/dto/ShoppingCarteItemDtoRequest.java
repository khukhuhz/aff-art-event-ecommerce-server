package com.khouloud.ecommerceapi.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ShoppingCarteItemDtoRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 0)
    private Integer quantity;
}
