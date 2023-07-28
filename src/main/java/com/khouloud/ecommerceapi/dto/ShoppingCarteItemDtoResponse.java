package com.khouloud.ecommerceapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ShoppingCarteItemDtoResponse {

    @NotNull
    private Long shoppingCarteItemId;

    @NotNull
    private ProductDtoResponse productDtoResponse;

    @NotNull
    private Integer quantity;

    @NotNull
    private LocalDateTime createdDate;
}
