package com.khouloud.ecommerceapi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ShoppingCarteDtoResponse {

    @NotNull
    List<ShoppingCarteItemDtoResponse> shoppingCarteItemDtoResponses;

    @NotNull
    Double totalPrice;

    @NotNull
    Integer totalNumberOfItems;
}
