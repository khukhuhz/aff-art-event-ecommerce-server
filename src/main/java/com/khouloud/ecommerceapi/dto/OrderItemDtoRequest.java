package com.khouloud.ecommerceapi.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDtoRequest {

    @NotNull
    private Long productId;

    @Min(value = 1)
    private Long quantity;
}
